#!/usr/local/bin/perl

use warnings;
use strict;
use Data::Dumper;

# Este programa intenta clasificar una serie de datos de log parseados en función de unas reglas en Drools también parseadas.
# Las reglas de políticas son del tipo:
#
#rule "policy-1 FLV"
#when
#	squid:Squid(dif_MCT=="video",dif_content=="video/x-flv")
#then
#	PolicyDecisionPoint.deny();
#end
#
# Del parseo de las reglas obtenemos un hash multidimensional de reglas con el siguiente formato:
#
#%reglas = (
#   regla => {
#       accion	  => deny, 
#       campo0	  => dif_MCT,
#	relacion0 => ==,
#	valor0	  => video
#       campo1	  => dif_content,
#	relacion1 => ==
#	valor1	  => video/x-flv,
#   },
#);
#

my $drlfile = "Initial-rules-squid.drl"; #Este es el fichero de reglas de Drools de Sergio

my %reglas = (); #Inicializar el hash
my @keys = ("accion", "campo", "relacion", "valor");
my $ind_regla = 0;
my $orden = "";


open (IN, "<$drlfile") or die "No existe el fichero ".$drlfile;
while (<IN>)
{
	my @argumentos = ();
	if ($_ =~ /^\D+\.(.+)\(\)\;/) {
		$orden = $1;
		$reglas{"regla".$ind_regla}{$keys[0]} = $orden; # Como la acción se lee después de las condiciones, hay que meterlo en el 
		$ind_regla++;					# hash antes de que pare a la siguiente regla.
		print "$_\n";		
	}
	if ($_ =~ /^\D+:\D+\((.+)\)/) {
		print "$_\n";
		for my $i (my @condiciones = split /,/, $1) {
			if ($i =~ /(.*)(==)"(.+)"/ || $i =~ /(.+)([>|<|=])(\d+)/ || $i =~ /(.+) (.+) "(.+)"/) {
				my @argtemp = ($1, $2, $3);
				push(@argumentos, @argtemp);
			}
		}	
	}

	my $ind_keys = 1;
	for my $j (0 .. $#argumentos) {
		my $temp = ($j/3)%3;
		$reglas{"regla".$ind_regla}{$keys[$ind_keys].$temp} = $argumentos[$j];
		$ind_keys++;
		if ($ind_keys == 4) {$ind_keys = 1;} 
#		print Dumper(\%reglas); # Para ver como se va rellenando el hash
	}
}
close IN;

# Del parseo de datos obtenemos otro hash multidimensional de formato:
#
#%logentradas = (
#   entrada => {
#       http_reply_code		=> 200, 
#       http_method		=> GET,
#	duration_miliseconds	=> 1114,
#	content_type		=> application/octet-stream,
#       server_or_cache_address	=> 192.168.4.4,
#	time 			=> 08:30:08,
#	squid_hierarchy		=> DEFAULT_PARENT,
#	bytes			=> 106961,
#	url			=> http://...,
#	client_address		=> 10.159.76.30
#   },
#);
#

my $logfile = "data_100k_instances_url_log_redux.csv"; #Fichero reducido de 50 entradas para pruebas
#my $logfile = "data_100k_instances_url_log.csv"; #Fichero de 100k entradas de log
my %logentradas = (); #Inicializar el hash de entradas de log

open (IN2, "<$logfile") or die "No existe el fichero ".$logfile;

my @keys = split /;/, <IN2>;     #Extraer las claves de la primera línea del fichero
for my $k (0 .. $#keys) { 
	$keys[$k] =~ /"(.+)"/;
	$keys[$k] = $1;
}

my $numentrada = 0;
my $count = 0;

while (<IN2>) {

my @datoslog = split /;/, $_;
for my $d (0 .. $#datoslog) { 
	if ($datoslog[$d] =~ /"(.+)"/) { $datoslog[$d] = $1; }
}

	for my $i (0 .. $#keys) {
		$count = $count + $i;
		$logentradas{"entrada".$numentrada}{$keys[$i]} = $datoslog[$i];
	}

	$numentrada++;

}

close IN2;

# Los dos hash:
# print Dumper(\%reglas);
# print Dumper(\%logentradas);

# Hay que tener en cuenta las equivalencias entre terminación de Squid/Drools y el campo que se busca en los datos. Tenemos que:
#
# dif_code	= http_code
# dif_met	= http_method
# dif_MCT	= MCT del content_type (en application/octet-stream el MCT = application)
# dif_content	= content_type
# dif_squid	= squid_hierarchy
# url		= url
# bytes		= bytes
#




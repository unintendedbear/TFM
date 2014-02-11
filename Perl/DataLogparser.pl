#!/usr/local/bin/perl

use warnings;
use strict;
use Data::Dumper;

my $logfile = "data_100k_instances_url_log_redux.csv"; #Fichero reducido de 50 entradas para pruebas
#my $logfile = "data_100k_instances_url_log.csv"; #Fichero de 100k entradas de log
my %logentradas = (); #Inicializar el hash de entradas de log

open (IN, "<$logfile") or die "No existe el fichero ".$logfile; #Abrir y leerlo

my @keys = split /;/, <IN>;     #Extraer las claves de la primera línea del fichero
for my $k (0 .. $#keys) { 
	$keys[$k] =~ /"(.+)"/;
	$keys[$k] = $1;
}
push (@keys, 'content_type_MCT'); # Entonces content_type_MCT está en $keys[$#keys]

my $numentrada = 0;
my $count = 0;

while (<IN>) {

my @datoslog = split /;/, $_;
for my $d (0 .. $#datoslog) { 
	if ($datoslog[$d] =~ /"(.+)"/) { 
		$datoslog[$d] = $1;
		if ($1 =~ /^(\w+-*\w+)[\/?]\w+/) {
			$logentradas{"entrada".$numentrada}{$keys[$#keys]} = $1;
		}
	}
}

	for my $i (0 .. $#keys-1) {
		$count = $count + $i;
		$logentradas{"entrada".$numentrada}{$keys[$i]} = $datoslog[$i];
	}

	$numentrada++;

}

close IN;

print Dumper(\%logentradas);

#foreach my $name (sort keys %logentradas) {
#print "$name: $logentradas{$name}{'url'}\n";
#$logentradas{$name}{'url'} =~ /^http:\/\/(\w+).(\w+).(\w+)[\/*]/;
#print "$1 - $2 - $3 \n";

#}

#!/usr/local/bin/perl

use warnings;
use strict;
use Data::Dumper;

my $logfile = "data_100k_instances_url_log_redux.csv"; #Fichero reducido de 50 entradas para pruebas
my %logentradas = (); #Inicializar el hash de entradas de log

open (IN, "<$logfile") or die "No existe el fichero ".$logfile; #Abrir y leerlo

my @keys = split /;/, <IN>;     #Extraer las claves de la primera línea del fichero
for my $k (0 .. $#keys) { 
	$keys[$k] =~ /"(.+)"/;
	$keys[$k] = $1;
}

#my @fichero = <IN>;
#shift @fichero;
#my $logspuros = join '',@fichero; #Resto de fichero sin las claves

#my @datoslog = split /;/, $logspuros;
#print "@datoslog\n";
my $numentrada = 0;
my $count = 0;

#for my $lol (0 .. $#keys) { print "llave: $keys[$lol] -> dato: $datoslog[$lol]\n"; }

while (<IN>) {

#print "$_\n";
my @datoslog = split /;/, $_;
for my $d (0 .. $#datoslog) { 
	if ($datoslog[$d] =~ /"(.+)"/) { $datoslog[$d] = $1; }
}

	for my $i (0 .. $#keys) {
		$count = $count + $i;
		$logentradas{"entrada".$numentrada}{$keys[$i]} = $datoslog[$i];
#		print "cuenta: $count\n";
#		print "entrada".$numentrada."\n$_\n".$datoslog[$count]."\n";
#		print "dato: $datoslog[$i]\n";
	}

	$numentrada++;

}

close IN;

print Dumper(\%logentradas);

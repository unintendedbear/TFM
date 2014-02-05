#!/usr/local/bin/perl

use warnings;
use strict;

my $drlfile = "Initial-rules-squid.drl"; #Este es el fichero de reglas de Drools de Sergio

#open (IN, "<$drlfile") or die "No existe el fichero ".$drlfile; #Abrir y leerlo
#while (<IN>)
#{
#	print $_;
#}
#close IN; #Esto es para prueba en realidad

#Mi idea es crear un hash de reglas tal que:
#Si la regla es
#rule "policy-1 FLV"
#when
#	squid:Squid(dif_MCT=="video",dif_content=="video/x-flv")
#then
#	PolicyDecisionPoint.deny();
#end
#Entonces 
#%reglas = (
#   deny => {
#       dif_MCT		=> "video",
#       dif_content	=> "video/x-flv",
#   },
#);

my %reglas = (); #Inicializar el hash
my $orden = "";


open (IN, "<$drlfile") or die "No existe el fichero ".$drlfile; #Abrir y leerlo
while (<IN>)
{
	if ($_ =~ /^\D+\.(.+)\(\)\;/) {
		$orden = $1;
		print "$orden\n";
	} #PROBLEMAZO: el deny() o allow() SE LEE DESPUÉS T_T
	if ($_ =~ /^\D+:\D+\((.+)\)/) {
		for my $i (my @condiciones = split /,/, $1) {
			if ($i =~ /(.*)(==)"(.+)"/ || $i =~ /(.+)([>|<|=])(\d+)/ || $i =~ /(.+) (.+) "(.+)"/) {
				print "$1\n";
				print "$2\n";
				print "$3\n";
			}
		}		
	}

#	$reglas{$orden}{$campo} = $valor;
}
close IN;



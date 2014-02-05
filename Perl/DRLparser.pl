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

open (IN, "<$drlfile") or die "No existe el fichero ".$drlfile; #Abrir y leerlo
while (<IN>)
{
	next unless s/^(.*?):\s*//;
	print $_;
}
close IN;



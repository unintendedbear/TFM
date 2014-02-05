#!/usr/local/bin/perl

use warnings;
use strict;
use Data::Dumper;

my $drlfile = "Initial-rules-squid.drl"; #Este es el fichero de reglas de Drools de Sergio

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
#       dif_MCT		=> {
#			relacion => "==",
#			valor => "video",
#			},
#       dif_content	=> {
#			relacion => "==",
#			valor => "video/x-flv",
#			},
#   },
#);
#
# Problema: ¿y la relación entre ellas? ~ pues hash de hash de hash

my %reglas = (); #Inicializar el hash
my @keys = ("accion", "campo", "relacion", "valor");
my $ind_regla = -1;
my $orden = "";


open (IN, "<$drlfile") or die "No existe el fichero ".$drlfile; #Abrir y leerlo
while (<IN>)
{
	my @argumentos = ();
	if ($_ =~ /^\D+\.(.+)\(\)\;/) {
		$orden = $1;		
	} #PROBLEMAZO: el deny() o allow() SE LEE DESPUÉS T_T
	if ($_ =~ /^\D+:\D+\((.+)\)/) {
		$ind_regla++;
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
#		print "regla".$ind_regla." ".$keys[$ind_keys].$temp." ".$argumentos[$j]."\n";
		$ind_keys++;
		if ($ind_keys == 4) {$ind_keys = 1;} 
		$reglas{"regla".$ind_regla}{$keys[0]} = $orden;
		print Dumper(\%reglas); #A ver como se va rellenando el hash
	}
}
close IN;

#print Dumper(\%reglas);



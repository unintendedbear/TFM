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
#   regla => {
#       accion	  => deny/allow, 
#       campo0	  => dif_MCT/bytes/url,...
#	relacion0 => ==/matches/</>
#	valor0	  => ...
#       campo1	  => dif_MCT/bytes/url,...
#	relacion1 => ==/matches/</>
#	valor1	  => ...
#   },
#);
#

my %reglas = (); #Inicializar el hash
my @keys = ("accion", "campo", "relacion", "valor");
my $ind_regla = 0;
my $orden = "";


open (IN, "<$drlfile") or die "No existe el fichero ".$drlfile; #Abrir y leerlo
while (<IN>)
{
	my @argumentos = ();
	if ($_ =~ /^\D+\.(.+)\(\)\;/) {
		$orden = $1;
		$reglas{"regla".$ind_regla}{$keys[0]} = $orden;
		$ind_regla++;	
	} #PROBLEMAZO: el deny() o allow() SE LEE DESPUÉS T_T #Fixed
	if ($_ =~ /^\D+:\D+\((.+)\)/) {
		for my $i (my @condiciones = split /,/, $1) {
			if ($i =~ /(.*)(==)"(.+)"/ || $i =~ /(.+)([>|<|=])(\d+)/ || $i =~ /(.+) (.+) "\*\.(.+)\.\*"/) {
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
#		print Dumper(\%reglas); #A ver como se va rellenando el hash
	}
}
close IN;

print Dumper(\%reglas);

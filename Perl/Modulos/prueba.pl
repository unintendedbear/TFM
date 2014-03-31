#!/usr/bin/perl

use 5.012;
use warnings;
use Data::Dumper;

use Rule_Parser;

my $file = shift || "Initial-rules-squid.drl";

#my (@cosas, %salida) = drl_rules_to_hash($file);
my @cosas = drl_rules_to_hash($file);

print "@cosas\n";

#my @claves = sort {$salida{$a} <=> $salida{$b}} keys %salida;

#for my $i (@claves) {
#	print "-----------$i-----------\n";
#	my @claves2 = sort keys %{ $salida{$i} };
#	for my $j (@claves2) {
#        	print "$j, $salida{$i}{$j}\n";
#	}
#}

#print Dumper(\%salida);

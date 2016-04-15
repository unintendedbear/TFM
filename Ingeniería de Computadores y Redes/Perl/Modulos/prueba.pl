#!/usr/bin/perl

use 5.012;
use warnings;
use Data::Dumper;
use Sort::Naturally;

use Rule_Parser;

my $file = shift || "Initial-rules-squid.drl";

my $rules = drl_rules_to_hash($file);

my @claves = sort {${$rules}{$a} <=> ${$rules}{$b}} keys %$rules;
@claves = nsort(@claves);

for my $i (@claves) {
	print "-----------$i-----------\n";
	my @claves2 = sort keys %{ ${$rules}{$i} };
	for my $j (@claves2) {
        	print "$j, ${$rules}{$i}{$j}\n";
	}
}

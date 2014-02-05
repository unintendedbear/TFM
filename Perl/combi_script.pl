#!/usr/local/bin/perl

#Script 2 para los datos de conexiones.

use warnings;
use strict;
use Algorithm::Combinatorics qw(subsets);

my %freq; 

open (my $IN, '<', 'numbers.txt') or die $!;
while (<$IN>) {
	chomp;
	my @words = sort split /\s+/;
	my @itemsets = subsets(\@words);
	for (@itemsets) {
		$freq{join(',', @$_)}++;
	}
}
close $IN;

for (sort {$freq{$b} <=> $freq{$a}} keys %freq) {	
	next if $_ eq ''; 
	print "$_ => $freq{$_}\n";
}

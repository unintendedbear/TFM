#!/usr/local/bin/perl

#Script para los datos de conexiones.

use warnings;
use strict;
use Tree::FP;
use POSIX qw(ceil);

my %words;
my $min_sup = 20;
my $min_conf = 60; 

open (my $IN, '<', 'list.txt') or die $!;
while (<$IN>) {
	chomp;
	$words{$_}++ for split /\s+/;
}
close $IN;

my @sorted = sort {$words{$b} <=> $words{$a}} keys %words; 
my $fptree = Tree::FP -> new(@sorted); 
$fptree -> set_support($min_sup/100);
$fptree -> set_confidence($min_conf/100);



open (my $IN, '<', 'list.txt') or die $!;
while (<$IN>) {
	chomp;
	$fptree -> insert_tree(split /\s+/) or die "Error insertando la fila $.: ", $fptree -> err;
}
close $IN;

my @rules = $fptree -> association_rules;
for (@rules) {
	next if $_ -> confidence < $fptree -> confidence;
	print '{', join(',', $_ -> left), '} => {', join(',', $_ -> right), '}', sprintf('support: %.2f', $_ -> support, $_ -> confidence), "\n";
}


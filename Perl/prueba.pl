#!/usr/local/bin/perl

#Programa de prueba

use warnings;
use strict;

my $hola = "mundo!";
print "Hola $hola\n";

my @a = (0, 1, 2, 3, 4, 5, 6);
my @b = ();

foreach (@a) {
	print "$_\n";
	$b[$_] = ($_/3)%3;
}

print "@b\n";

#!/usr/local/bin/perl

#Programa de prueba

use warnings;
use strict;

my $hola = "mundo!";
print "Hola $hola\n";

# String with names inside square brackets
my $string = "Hello [Peter] , [Sophie] and [Jack] are here.";

pos($string) = 0;
while (my $string =~ m{\G.*\[([^\]+)\]}cg)
{
    my $name = $1;
    print "Found name $name .\n";
}

#!/usr/local/bin/perl

#Programa de prueba

use warnings;
use strict;
use Data::Dumper;

my $hola = "mundo!";
print "Hola $hola\n";

print "-------\n";

my @a = (0, 1, 2, 3, 4, 5, 6);
my @b = ();

foreach (@a) {
	print "$_\n";
	$b[$_] = ($_/3)%3;
}

print "@b\n";

print "-------\n";

print "$#aº valor de a: $a[$#a]\n";

print "-------\n";

for my $i (0 .. $#a-1) {
	print "contador $i\n";
}

print "-------\n";

my %diccionario;
$diccionario{"dif_code"} = "http_code";
$diccionario{"dif_met"} = "http_method";
$diccionario{"dif_MCT"} = "content_type";
$diccionario{"dif_content"} = "content_type";
$diccionario{"dif_squid"} = "squid_hierarchy";
$diccionario{"url"} = "url";
$diccionario{"bytes"} = "bytes";

print Dumper(\%diccionario);



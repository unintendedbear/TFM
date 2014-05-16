#!/usr/bin/perl

use warnings;
use strict;
use File::Slurp qw(read_file);
use Carp qw(croak);

my $una_url = "200;GET;1114;application/octet-stream;192.168.4.4;8:30:08;DEFAULT_PARENT;106961;http://au.download.windowsupdate.com/msdownload/update/software/uprl/2013/02/wu-windows6.1-kb2729094-v2-x64_7d08944484d693e51abaf9c37ec5b54019309e22.exe;10.159.76.30;allow";
my $otra_url = "200;GET;877;application/octet-stream;8.27.153.126;8:30:13;DIRECT;1529759;http://au.download.windowsupdate.com/msdownload/update/software/uprl/2013/02/wu-windows6.1-kb2729094-v2-x64_7d08944484d693e51abaf9c37ec5b54019309e22.exe;192.168.4.4;allow";

if ($una_url =~ s/(https?:\/\/([-\w\.]+)+(:\d+)?(\/([\w\/\_\-\.]*(\?\S+)?)?)?)/$1/) { $una_url = $1; }
if ($otra_url =~ s/(https?:\/\/([-\w\.]+)+(:\d+)?(\/([\w\/\_\-\.]*(\?\S+)?)?)?)/$1/) { $otra_url = $1; }

#if ($una_url eq $otra_url) { print "COINCIDENCIA\n"; }

#print "Primera:\n$una_url\nY segunda:$otra_url\n";

my @array = (0, 1, 2, 0, 1, 6, 1, 0, 1, 0, 2, 3, 4, 5, 5, 3, 4, 2, 1, 6);
my @otro_array = ();
my @otro_array2 = ();
my $control = 1;
my $count = 0;

my $train_size = sprintf("%.0f", ($#array + 1)*8/10);
my $test_size = sprintf("%.0f", ($#array + 1)*2/10);
print "Del total de $#array, tamaño TRAIN $train_size y tamaño TEST $test_size\n";

while ($#array >= 0) {
	
	my $numero = shift @array;
	print "Número------$numero\n";
	$control = 1;
	foreach (@otro_array) {
		#print "COMPARANDO $numero CON $_ EN \@OTRO_ARRAY\n";
		if ($numero == $_) {
			#print "Nos salimos porque hemos encontrado coincidencia EN \@OTRO_ARRAY\n";
			push (@otro_array, $numero);
			$train_size--;
			print "\$train_size $train_size\n";
			$control = 0;
			last;
		}
	}
	if ($control == 1) {
	foreach (@otro_array2) {
		#print "COMPARANDO $numero CON $_ EN \@OTRO_ARRAY2\n";
		if ($numero == $_) {
			#print "Nos salimos porque hemos encontrado coincidencia EN \@OTRO_ARRAY2\n";
			push (@otro_array2, $numero);
			$test_size--;
			print "\$test_size $test_size\n";
			$control = 0;
			last;
		}
	}}
	if ($control == 1) {
		my $number = int(rand(10));
		if ($number < 8 && $train_size > 0) {
			push (@otro_array, $numero);
			$train_size--;
			print "\$train_size $train_size\n";
		} elsif ($test_size > 0) {
			push (@otro_array2, $numero);
			$test_size--;
			print "\$test_size $test_size\n";
		}
	}
	$count++;
}

print "\@otro_array vale: @otro_array\n";
print "\@otro_array2 vale: @otro_array2\n";
my $suma = $#otro_array+$#otro_array2;
print "total: $suma\n";

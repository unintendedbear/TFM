#!/usr/bin/perl

use warnings;
use strict;
use File::Slurp qw(read_file);
use Carp qw(croak);

my $in_file = shift || croak "Please specify the .csv file";
my $out_file;
if ($in_file =~ /(.+)\.csv/) { $out_file = "$1_Undersampled.csv"}
print "$out_file\n";

my $log_file = read_file( $in_file );

my @in_rows = split(/\s+/, $log_file);
my @out_rows = ();

open (OUT, ">$out_file") or die "No existe el fichero ".$out_file;

for my $line ( @in_rows ) {
	
	if ( $line =~ m/allow/ ) {
		my $number = int(rand(2));
		if ($number == 0) { print OUT "$line\n"; }		
	} else {
		print OUT "$line\n";
	}
}

close OUT;

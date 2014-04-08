#!/usr/bin/perl

use warnings;
use strict;
use File::Slurp qw(read_file);
use Carp qw(croak);

my $in_file = shift || croak "Please specify the .csv file";
my $percentage_training = shift || croak "Please specify the percentage of the Training dataset";
my $percentage_test;
if ($percentage_training =~ /(.+)\%/) {
	$percentage_training = $1;
	$percentage_test = 100-$1;
} else {
	$percentage_test = 100-$percentage_training;
}
my $out_file_training;
my $out_file_test;
if ($in_file =~ /(.+)\.csv/) {
	$out_file_training = "$1_Training.csv";
	$out_file_test = "$1_Test.csv";
}

my $log_file = read_file( $in_file );

my @in_rows = split(/\n+/, $log_file);
my @out_rows = ();

open (OUT_TRN, ">$out_file_training") or die "No existe el fichero ".$out_file_training;
open (OUT_TST, ">$out_file_test") or die "No existe el fichero ".$out_file_test;

for my $line ( @in_rows ) {
	
	if ( $line =~ m/allow/ || $line =~ m/deny/ ) {
		my $number = int(rand(10));
		if ($number < 2) {
			print OUT_TST "$line\n";		
		} else {
			print OUT_TRN "$line\n";
		}
	} else {
		print OUT_TST "$line\n";
		print OUT_TRN "$line\n";
	}
}

close OUT_TRN;
close OUT_TST;

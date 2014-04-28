#!/usr/bin/perl

use warnings;
use strict;
use File::Slurp qw(read_file);
use Carp qw(croak);

my $in_file = shift || croak "Please specify the .csv file";
my $percentage_training = shift || croak "Please specify the percentage of the Training dataset";
my $option = shift || croak "Please specify \'random\' or \'consequent\'";
my $type = shift || croak "Please specify in the file has been \'(over)\'sampled or \'(under)\'sampled";
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
	$out_file_training = "$1_Training_"."$percentage_training"."_"."$option.csv";
	$out_file_test = "$1_Test_"."$percentage_test"."_"."$option.csv";
}

my $log_file = read_file( $in_file );

my @in_rows = split(/\n+/, $log_file);
my @out_rows = ();
my $allows = how_many_allows(@in_rows);
my $denies = how_many_denies(@in_rows);

my $allows_trn = sprintf("%.0f", $allows*($percentage_training/100));
my $denies_trn = sprintf("%.0f", $denies*($percentage_training/100));

open (OUT_TRN, ">$out_file_training") or die "No existe el fichero ".$out_file_training;
open (OUT_TST, ">$out_file_test") or die "No existe el fichero ".$out_file_test;

my $heading = shift @in_rows;
print OUT_TST "$heading\n";		
print OUT_TRN "$heading\n";

if ($option eq "random") {

	while ( $#in_rows > 0 ) {

		if ($type eq "under" || $type eq "undersampled") {
			my $line = shift @in_rows;
			my $number = int(rand(10));
			if ($number < $percentage_test/10) {
				print OUT_TST "$line\n";		
			} else {
				print OUT_TRN "$line\n";
			}
		} else {
			my $line = shift @in_rows;
			if ( $line =~ m/deny$/ ) {
				my $repeated_line = shift @in_rows;
				my $number = int(rand(10));
				if ($number < $percentage_test/10) {
					print OUT_TST "$line\n";	
					print OUT_TST "$repeated_line\n";		
				} else {
					print OUT_TRN "$line\n";
					print OUT_TRN "$repeated_line\n";
				}
			} else {
				my $number = int(rand(10));
				if ($number < $percentage_test/10) {
					print OUT_TST "$line\n";		
				} else {
					print OUT_TRN "$line\n";
				}
			}
		}
	}
} else {
#consecutivos en lugar de aleatorios
	
	while ( $#in_rows > 0 ) {

		if ($type eq "under" || $type eq "undersampled") {
			my $line = shift @in_rows;
			if ( $line =~ m/allow$/ && $allows_trn > 0 ) {
				print OUT_TRN "$line\n";
				$allows_trn--;
			} elsif ( $line =~ m/deny$/ && $denies_trn > 0 ) {
				print OUT_TRN "$line\n";
				$denies_trn--;
			} else {
				print OUT_TST "$line\n";
			}
		} else {

			my $line = shift @in_rows;
			if ( $line =~ m/allow$/ && $allows_trn > 0 ) {
				print OUT_TRN "$line\n";
				$allows_trn--;
			} elsif ( $line =~ m/deny$/ && $denies_trn > 0 ) {
				my $repeated_line = shift @in_rows;
				print OUT_TRN "$line\n";
				print OUT_TRN "$repeated_line\n";
				$denies_trn = $denies_trn-2;
			} else { 
				print OUT_TST "$line\n";
				if ( $line =~ m/deny$/ ) {				
					my $repeated_line = shift @in_rows;
					print OUT_TST "$repeated_line\n";
				}
			}
		}
	}
}

close OUT_TRN;
close OUT_TST;

sub how_many_allows {
	my $count_allow = 0;

	for my $i ( @_ ) {
		if ( $i =~ m/allow$/) {
			$count_allow++;
		}
	}
	return $count_allow;
}

sub how_many_denies {

	my $count_deny = 0;

	for my $i ( @_ ) {	
		if ( $i =~ m/deny$/) {
			$count_deny++;
		}
	}
	return $count_deny;
}

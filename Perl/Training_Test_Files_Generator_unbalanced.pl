#!/usr/bin/perl

use warnings;
use strict;
use File::Slurp qw(read_file);
use Carp qw(croak);

my $in_file = shift || croak "Please specify the .csv file";
my $percentage_training = shift || croak "Please specify the percentage of the Training dataset";
my $option = shift || croak "Please specify \'random\' or \'consecutive\'";
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
my @train_entries = ();
my @test_entries = ();

print OUT_TST "$heading\n";		
print OUT_TRN "$heading\n";

if ($option eq "random") {

	my $train_size = sprintf("%.0f", ($#in_rows + 1)*$percentage_training/100);
	my $test_size = sprintf("%.0f", ($#in_rows + 1)*$percentage_test/100);
	print "Inicialmente: \$train_size: $train_size y \$test_size: $test_size\n";
	while ( $#in_rows >= 0 ) {
		my $line = shift @in_rows;
		my $control = 1;
		foreach (@train_entries) {
			my $url;
			if ($line =~ /(https?:\/\/([-\w\.]+)+(:\d+)?(\/([\w\/\_\-\.]*(\?\S+)?)?)?)/) { $url = $1; }
			my $url_cmp;
			if (/(https?:\/\/([-\w\.]+)+(:\d+)?(\/([\w\/\_\-\.]*(\?\S+)?)?)?)/) { $url_cmp = $1; }
			if ($url eq $url_cmp) {
				push (@train_entries, $line);
				$train_size--;
				$control = 0;
				last;
			}
		}
		if ($control == 1) {
			foreach (@test_entries) {
				my $url;
				if ($line =~ /(https?:\/\/([-\w\.]+)+(:\d+)?(\/([\w\/\_\-\.]*(\?\S+)?)?)?)/) { $url = $1; }
				my $url_cmp;
				if (/(https?:\/\/([-\w\.]+)+(:\d+)?(\/([\w\/\_\-\.]*(\?\S+)?)?)?)/) { $url_cmp = $1; }
				if ($url eq $url_cmp) {
					push (@test_entries, $line);
					$test_size--;
					$control = 0;
					last;
				}
			}
		}
		if ($control == 1) {
			my $number = int(rand(10));
			if ($number > $percentage_test/10 && $train_size > 0) {
				push (@train_entries, $line);
				$train_size--;
			} elsif ($test_size > 0) {
				push (@test_entries, $line);
				$test_size--;
			} else {
				push (@train_entries, $line);
				$train_size--;
			}
		}
	}

	foreach (@train_entries) { print OUT_TRN "$_\n"; }
	foreach (@test_entries) { print OUT_TST "$_\n"; }
	
	print "Nos queda: \$train_size: $train_size y \$test_size: $test_size\n";
} else {
#consecutivos en lugar de aleatorios
	
	while ( $#in_rows > 0 ) {

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

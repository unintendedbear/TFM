#!/usr/bin/perl

use warnings;
use strict;
use File::Slurp qw(read_file);

my $in_file = shift || "data_100k_instances_url_log_Allow-Deny_labels.csv";
my $out_file;
if ($in_file =~ /(.+)\.csv/) { $out_file = "$1_Oversampled.csv"}
print "$out_file\n";

my $log_file = read_file( $in_file );

my @in_rows = split(/\s+/, $log_file);
my @out_rows = ();

open (OUT, ">$out_file") or die "No existe el fichero ".$out_file;

for my $line ( @in_rows ) {
	
	if ( $line =~ m/deny/ ) {
		print OUT "$line\n";
		print OUT "$line\n";
	} else {
		print OUT "$line\n";
	}
}

close OUT;

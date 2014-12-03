#!/usr/bin/perl

use warnings;
use strict;
use File::Slurp qw(read_file);
use Carp qw(croak);
use Switch;

my $file = shift || croak "Please specify a file.";

my @rules = read_file( $file );
my %features;
my $instances;
my $classifier;
my @partfeatures = ();
my @j48rule = ();

for my $rule ( @rules ) {

	if ( $rule =~ /(.*)\n/ ) {
		$rule = $1;
	}

	if ( $rule =~ /Scheme:\s*([\w\.]+)/ ) { #Which classifier?
		my @attributes = split (/\./, $1);
		$classifier = $attributes[$#attributes];
	}

	if ( $rule =~ /Instances:\s+(\d+)/ ) { #Number of instances
		$instances = $1;
	}

	switch ($classifier) {

		case "JRip" {
			######## JRIP ########
			if ( $rule =~ /^\(\w+[\s\>\=\<]+[\w\.]+\)/ ) {
				my @components = split (/\sand\s/, $rule);
				my $weight;
				my @subcomponents = split (/\s/, $components[$#components]);
				if ( $subcomponents[$#subcomponents] =~ /\((\d+)\.\d+\/\d+\.\d+\)/ ) {
					$weight = $1/$instances;
				}
				for my $component ( @components ) {
					if ( $component =~ /^\((\w+)[\s\>\=\<]+[\w\.]+\)/ ) {
						$features{$1} += $weight;
					}
				}
			}
		}
		case "PART" {
			######## PART ########
			if ( $rule =~ /^(\w+)[\s\>\=\<]+\w+\sAND$/ ) {
				print "$rule\n";
				push (@partfeatures, $1);
			}

			if ( $rule =~ /^(\w+)[\s\>\=\<]+\w+\:\s\w+\s\((\d+)\.\d+\/?\d*\.*\d*\)$/ ) {
				print "$rule\n";
				push (@partfeatures, $1);
				my $weight = $2/$instances;

				for my $i ( @partfeatures ) {
					print "$i,";
					$features{$i} += $weight;
				}
				print "\n";
				@partfeatures = ();
			}
		}
		case "J48" {
			######## J48 ########
			if ( $rule =~ /^(\w+)[\s\>\=\<]+\w+\:\s\w+\s\((\d+)\.\d+\/?\d*\.*\d*\)$/ ) {
				print "Regla tal cual:\n$rule\n";
			}

			if ( $rule =~ /^(\w+)[\s\>\=\<]+\w+$/ ) {
				print "Primer atributo ($1) de una regla:\n"."$rule";
			}

			if ( $rule =~ /^\|\s+(\w+)[\s\>\=\<]+\w+$/ ) {				
				print " AND $rule";
			}

			if ( $rule =~ /^\|\s+\|\s+(\w+)[\s\>\=\<]+\w+\:\s\w+\s\((\d+)\.\d+\/?\d*\.*\d*\)$/ ) {				
				print " AND $rule\n";
			}

		}
		case "REPTree" {

		}

	}
}

my @keys = sort { $features{$a} <=> $features{$b} } keys(%features);
print "Ranking:\n";
for my $key ( @keys ) {
	print "$key --- $features{$key}\n";
}

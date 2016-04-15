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
my @reprule = ();

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
				if ( $subcomponents[$#subcomponents] =~ /\((\d+)\.\d+\/(\d+)\.\d+\)/ ) {
					if ($2 eq '') { $weight = $1/$instances; } else { $weight = ($1-$2)/$instances; }
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

			if ( $rule =~ /^(\w+)[\s\>\=\<]+\w+\:\s\w+\s\((\d+)\.\d+\/?(\d*)\.*\d*\)$/ ) {
				print "$rule\n";
				push (@partfeatures, $1);
				my $weight;
				if ($3 eq '') { $weight = $2/$instances; } else { $weight = ($2-$3)/$instances; }

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
			if ($rule =~ /\w+\s[\>\=\<]+\s\w+/ || $rule =~ /\|[\|\s]+(.*)/ ) {
			my @rulediscovery = split (/\s+/, $rule);
			my $order = 0;

			for my $j ( @rulediscovery) {
				if ( $j =~ /\|/ ) {
					$order++;
				}
			}

			switch ($order) {
				case 0 {
					if ( $#rulediscovery > $order+2) {
						# Caso
						# url_core = windowsupdate: allow (865.5)
						$rulediscovery[$order+4] =~ /\((\d+)\.\d+\/?(\d*)\.*\d*\)/;
						my $weight;
						if ($2 eq '') { $weight = $1/$instances; } else { $weight = ($1-$2)/$instances; }
						$features{$rulediscovery[$order]} += $weight;
					} else {
						# Caso
						# url_core = doubleclick
						@j48rule = ();
						push (@j48rule, $rulediscovery[$order]);
					}
				}
				case 1 {
					if ( $#rulediscovery > $order+2) {
						# Caso
						# url_core = doubleclick
						# |   content_type_MCT = application: allow (1.07)
						$rulediscovery[$order+4] =~ /\((\d+)\.\d+\/?(\d*)\.*\d*\)/;
						if ( $#j48rule == $order ) {
							pop @j48rule;
							push (@j48rule, $rulediscovery[$order]);
						} elsif ( $#j48rule > $order ) {
							my $difference = $#j48rule - $order;
							for my $step (0 .. $difference) {
								pop @j48rule;
							}
							push (@j48rule, $rulediscovery[$order]);
						} else {
							push (@j48rule, $rulediscovery[$order]);
						}
						my $weight;
						if ($2 eq '') { $weight = $1/$instances; } else { $weight = ($1-$2)/$instances; }
						for my $k ( @j48rule ) {
							$features{$k} += $weight;
						}
						pop @j48rule;
					} else {
						# Caso
						# url_core = doubleclick
						# |   num_subdomains <= 1
						if ( $#j48rule == $order ) {
							pop @j48rule;
							push (@j48rule, $rulediscovery[$order]);
						} elsif ( $#j48rule > $order ) {
							my $difference = $#j48rule - $order;
							for my $step (0 .. $difference) {
								pop @j48rule;
							}
							push (@j48rule, $rulediscovery[$order]);
						} else {
							push (@j48rule, $rulediscovery[$order]);
						}
					}
				}
				case 2 {
					if ( $#rulediscovery > $order+2) {
						# Caso
						# url_core = doubleclick
						# |   num_subdomains <= 1
						# |   |   content_type_MCT = application: allow (1.07)
						$rulediscovery[$order+4] =~ /\((\d+)\.\d+\/?(\d*)\.*\d*\)/;
						if ( $#j48rule == $order ) {
							pop @j48rule;
							push (@j48rule, $rulediscovery[$order]);
						} elsif ( $#j48rule > $order ) {
							my $difference = $#j48rule - $order;
							for my $step (0 .. $difference) {
								pop @j48rule;
							}
							push (@j48rule, $rulediscovery[$order]);
						} else {
							push (@j48rule, $rulediscovery[$order]);
						}
						my $weight;
						if ($2 eq '') { $weight = $1/$instances; } else { $weight = ($1-$2)/$instances; }
						for my $k ( @j48rule ) {
							$features{$k} += $weight;
						}
						pop @j48rule;
					} else {
						# Caso
						# url_core = doubleclick
						# |   num_subdomains > 1
						# |   |   digits_in_URL <= 16
						if ( $#j48rule == $order ) {
							pop @j48rule;
							push (@j48rule, $rulediscovery[$order]);
						} elsif ( $#j48rule > $order ) {
							my $difference = $#j48rule - $order;
							for my $step (0 .. $difference) {
								pop @j48rule;
							}
							push (@j48rule, $rulediscovery[$order]);
						} else {
							push (@j48rule, $rulediscovery[$order]);
						}
					}
				}
				case 3 {
					if ( $#rulediscovery > $order+2) {
						# Caso
						# url_core = doubleclick
						# |   num_subdomains > 1
						# |   |   digits_in_URL <= 16
						# |   |   |   subdomain2 = au: allow (0.0)
						$rulediscovery[$order+4] =~ /\((\d+)\.\d+\/?(\d*)\.*\d*\)/;
						if ( $#j48rule == $order ) {
							pop @j48rule;
							push (@j48rule, $rulediscovery[$order]);
						} elsif ( $#j48rule > $order ) {
							my $difference = $#j48rule - $order;
							for my $step (0 .. $difference) {
								pop @j48rule;
							}
							push (@j48rule, $rulediscovery[$order]);
						} else {
							push (@j48rule, $rulediscovery[$order]);
						}
						my $weight;
						if ($2 eq '') { $weight = $1/$instances; } else { $weight = ($1-$2)/$instances; }
						for my $k ( @j48rule ) {
							$features{$k} += $weight;
						}
						pop @j48rule;
					} else {
						# Caso
						# url_core = doubleclick
						# |   num_subdomains > 1
						# |   |   digits_in_URL <= 16
						# |   |   |   subdomain2 = ad
						if ( $#j48rule == $order ) {
							pop @j48rule;
							push (@j48rule, $rulediscovery[$order]);
						} elsif ( $#j48rule > $order ) {
							my $difference = $#j48rule - $order;
							for my $step (0 .. $difference) {
								pop @j48rule;
							}
							push (@j48rule, $rulediscovery[$order]);
						} else {
							push (@j48rule, $rulediscovery[$order]);
						}
					}
				}
				case 4 {
					if ( $#rulediscovery > $order+2) {
						# Caso
						# url_core = doubleclick
						# |   num_subdomains > 1
						# |   |   digits_in_URL <= 16
						# |   |   |   subdomain2 = ad
						# |   |   |   |   content_type_MCT = application: deny (6.0)
						$rulediscovery[$order+4] =~ /\((\d+)\.\d+\/?(\d*)\.*\d*\)/;
						if ( $#j48rule == $order ) {
							pop @j48rule;
							push (@j48rule, $rulediscovery[$order]);
						} elsif ( $#j48rule > $order ) {
							my $difference = $#j48rule - $order;
							for my $step (0 .. $difference) {
								pop @j48rule;
							}
							push (@j48rule, $rulediscovery[$order]);
						} else {
							push (@j48rule, $rulediscovery[$order]);
						}
						my $weight;
						if ($2 eq '') { $weight = $1/$instances; } else { $weight = ($1-$2)/$instances; }
						for my $k ( @j48rule ) {
							$features{$k} += $weight;
						}
						pop @j48rule;
					} else {
						# Caso
						# url_core = google
						# |   num_subdomains <= 1
						# |   |   letters_in_URL > 178
						# |   |   |   bytes > 601
						# |   |   |   |   http_reply_code = 200
						if ( $#j48rule == $order ) {
							pop @j48rule;
							push (@j48rule, $rulediscovery[$order]);
						} elsif ( $#j48rule > $order ) {
							my $difference = $#j48rule - $order;
							for my $step (0 .. $difference) {
								pop @j48rule;
							}
							push (@j48rule, $rulediscovery[$order]);
						} else {
							push (@j48rule, $rulediscovery[$order]);
						}
					}
				}
				case 5 {
					if ( $#rulediscovery > $order+2) {
						# Caso
						# url_core = google
						# |   num_subdomains <= 1
						# |   |   letters_in_URL > 178
						# |   |   |   bytes > 601
						# |   |   |   |   http_reply_code = 200
						# |   |   |   |   |   digits_in_URL <= 81: allow (182.0/3.0)
						$rulediscovery[$order+4] =~ /\((\d+)\.\d+\/?(\d*)\.*\d*\)/;
						if ( $#j48rule == $order ) {
							pop @j48rule;
							push (@j48rule, $rulediscovery[$order]);
						} elsif ( $#j48rule > $order ) {
							my $difference = $#j48rule - $order;
							for my $step (0 .. $difference) {
								pop @j48rule;
							}
							push (@j48rule, $rulediscovery[$order]);
						} else {
							push (@j48rule, $rulediscovery[$order]);
						}
						my $weight;
						if ($2 eq '') { $weight = $1/$instances; } else { $weight = ($1-$2)/$instances; }
						for my $k ( @j48rule ) {
							$features{$k} += $weight;
						}
						pop @j48rule;
					} else {
						# Caso
						# url_core = google
						# |   num_subdomains <= 1
						# |   |   letters_in_URL > 178
						# |   |   |   bytes > 601
						# |   |   |   |   http_reply_code = 200
						# |   |   |   |   |   digits_in_URL > 81
						if ( $#j48rule == $order ) {
							pop @j48rule;
							push (@j48rule, $rulediscovery[$order]);
						} elsif ( $#j48rule > $order ) {
							my $difference = $#j48rule - $order;
							for my $step (0 .. $difference) {
								pop @j48rule;
							}
							push (@j48rule, $rulediscovery[$order]);
						} else {
							push (@j48rule, $rulediscovery[$order]);
						}
					}
				}
				case 6 {
					if ( $rulediscovery[$order+2] =~ /\w+\:/ && $#rulediscovery > $order+2) {
						# Caso
						# url_core = google
						# |   num_subdomains <= 1
						# |   |   letters_in_URL > 178
						# |   |   |   bytes > 601
						# |   |   |   |   http_reply_code = 200
						# |   |   |   |   |   digits_in_URL > 81
						# |   |   |   |   |   |   bytes <= 973: allow (4.08)
						$rulediscovery[$order+4] =~ /\((\d+)\.\d+\/?(\d*)\.*\d*\)/;
						if ( $#j48rule == $order ) {
							pop @j48rule;
							push (@j48rule, $rulediscovery[$order]);
						} elsif ( $#j48rule > $order ) {
							my $difference = $#j48rule - $order;
							for my $step (0 .. $difference) {
								pop @j48rule;
							}
							push (@j48rule, $rulediscovery[$order]);
						} else {
							push (@j48rule, $rulediscovery[$order]);
						}
						my $weight;
						if ($2 eq '') { $weight = $1/$instances; } else { $weight = ($1-$2)/$instances; }
						for my $k ( @j48rule ) {
							$features{$k} += $weight;
						}
						pop @j48rule;
					}
				}
			}}		

		}
		case "REPTree" {			
			######## REPTree ########
			if ($rule =~ /\w+\s[\>\=\<]+\s\w+/ || $rule =~ /\|[\|\s]+(.*)/ ) {
			my @rulediscovery = split (/\s+/, $rule);
			my $order = 0;

			for my $j ( @rulediscovery) {
				if ( $j =~ /\|/ ) {
					$order++;
				}
			}

			switch ($order) {
				case 0 {
					if ( $#rulediscovery > $order+2) {
						# Caso
						# url_core = windowsupdate : allow (581.33/0) [284.18/0]
						$rulediscovery[$order+5] =~ /\((\d+)\.?\d*\/?(\d*)\.*\d*\)/;
						my $weight;
						if ($2 eq '') { $weight = $1/$instances; } else { $weight = ($1-$2)/$instances; }
						$features{$rulediscovery[$order]} += $weight;
					} else {
						# Caso
						# url_core = doubleclick
						@reprule = ();
						push (@reprule, $rulediscovery[$order]);
					}
				}
				case 1 {
					if ( $#rulediscovery > $order+2) {
						# Caso
						# url_core = doubleclick
						# |   time = 08:30:08 : deny (0/0) [0/0]
						$rulediscovery[$order+5] =~ /\((\d+)\.?\d*\/?(\d*)\.*\d*\)/;
						if ( $#reprule == $order ) {
							pop @reprule;
							push (@reprule, $rulediscovery[$order]);
						} elsif ( $#reprule > $order ) {
							my $difference = $#reprule - $order;
							for my $step (0 .. $difference) {
								pop @reprule;
							}
							push (@reprule, $rulediscovery[$order]);
						} else {
							push (@reprule, $rulediscovery[$order]);
						}
						my $weight;
						if ($2 eq '') { $weight = $1/$instances; } else { $weight = ($1-$2)/$instances; }
						for my $k ( @reprule ) {
							$features{$k} += $weight;
						}
						pop @reprule;
					} else {
						# Caso
						# url_core = doubleclick
						# |   time = 09:58:23
						if ( $#reprule == $order ) {
							pop @reprule;
							push (@reprule, $rulediscovery[$order]);
						} elsif ( $#reprule > $order ) {
							my $difference = $#reprule - $order;
							for my $step (0 .. $difference) {
								pop @reprule;
							}
							push (@reprule, $rulediscovery[$order]);
						} else {
							push (@reprule, $rulediscovery[$order]);
						}
					}
				}
				case 2 {
					if ( $#rulediscovery > $order+2) {
						# Caso
						# url_core = doubleclick
						# |   time = 09:58:23
						# |   |   http_reply_code = 204 : allow (0/0) [0/0]
						$rulediscovery[$order+5] =~ /\((\d+)\.?\d*\/?(\d*)\.*\d*\)/;
						if ( $#reprule == $order ) {
							pop @reprule;
							push (@reprule, $rulediscovery[$order]);
						} elsif ( $#reprule > $order ) {
							my $difference = $#reprule - $order;
							for my $step (0 .. $difference) {
								pop @reprule;
							}
							push (@reprule, $rulediscovery[$order]);
						} else {
							push (@reprule, $rulediscovery[$order]);
						}
						my $weight;
						if ($2 eq '') { $weight = $1/$instances; } else { $weight = ($1-$2)/$instances; }
						for my $k ( @reprule ) {
							$features{$k} += $weight;
						}
						pop @reprule;
					}
				}
			}}
		}

	}
}

my @keys = sort { $features{$a} <=> $features{$b} } keys(%features);
my $outputfile = "Output.csv";
open (OUT, ">$outputfile") or die "No existe el fichero ".$outputfile;
print OUT "Feature,$classifier\n";
for my $key ( @keys ) {
	print OUT "$key,$features{$key}\n";
}
close OUT;

#!/usr/bin/perl

use warnings;
use strict;
use File::Slurp qw(read_file);
use Carp qw(croak);

my @names = ('Intro', 'Foo', 'Foo', 'Bar', 'Bar', 'Moo', 'Moo');

my $intro = shift @names;
print "Leo: $intro\n";

while ($#names > 0) {
	my $number = int(rand(2));
	if ($number < 1) {
		my $saco = shift @names;
		print "Entrenamiento: $saco\n"; # Foo
		my $sacootro = shift @names;
		print "Entrenamiento: $sacootro\n"; # Bar Moo
	} else {
		my $saco = shift @names;
		print "Test: $saco\n"; # Foo
		my $sacootro = shift @names;
		print "Test: $sacootro\n"; # Bar Moo
	}
}

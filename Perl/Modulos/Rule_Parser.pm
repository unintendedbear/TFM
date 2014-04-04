package Rule_Parser;

use 5.012;
use warnings;
use File::Slurp qw(read_file);
use Carp qw(croak);
use constant KEYS => qw(
field relation value action
);
use base 'Exporter';
our @EXPORT = qw(drl_rules_to_hash);

sub drl_rules_to_hash {
	my $file = shift || croak "Please specify a valid Drools file.";

	my %rules;
	my $key_index = 0;
	my $rule_count = 0;
	my @rows = read_file( $file );
	my @cosas = ();

	for my $line ( @rows ) {
		my @arguments = ();
		#push (@cosas, $line);
		if ( $line =~ /^\D+:\D+\((.+)\)/ ) {
			push (@cosas, $line);			
			my @conditions = split (/,/, $1);
			for my $i ( @conditions ) {
				if ($i =~ /\s*(.*)(==)"(.+)"/ || $i =~ /\s*(.+)([>|<|=])(\d+)/ || $i =~ /\s*(.+) (.+) "?\*\.(.+)\.\*"?/) {
					push (@arguments, ($1, $2, $3));
				}
			}
		}

		if ( $line =~ /^\D+\.(.+)\(\)\;/) {
			push (@arguments, $1);
			$rule_count++;
		}

		for my $j (0 .. $#arguments) {
			my $temp = ($j/3)%4;
			my $key = (KEYS)[$key_index];
			$rules{"rule".$rule_count}{$key.$temp} = $arguments[$j];
			$key_index++;
			if ($key_index == 4) {$key_index = 0;}
		}
	}

	return @cosas;
	#return (@cosas, %rules);
}

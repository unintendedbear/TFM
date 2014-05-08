package Data_Parser;

use 5.012;
use warnings;
use File::Slurp qw(read_file);
use Carp qw(croak);
use constant KEYS => qw(
http_reply_code http_method duration_milliseconds content_type_MCT content_type server_or_cache_address time squid_hierarchy bytes url_core client_address
);
use base 'Exporter';
our @EXPORT = qw(log_data_to_hash);


sub log_data_to_hash {
	my $file = shift || croak "Please specify a valid Drools file.";

	my $rule_count = 0;
	my @rows = read_file( $file );
	my @cosas = ();	
	my @arguments = ();
	my %rules;

	for my $line ( @rows ) {		
		if ( $line =~ /^\D+:\D+\((.+)\)/ ) {
			push (@cosas, $line);
			my @conditions = split (/,/, $1);
			for my $i ( @conditions ) {
				if ($i =~ /\s*(.*)(==)"(.+)"/ || $i =~ /\s*(.+)([>|<|=])(\d+)/ || $i =~ /\s*(.+) (.+) "?\*\.(.+)\.\*"?/) {
					#push (@cosas, ($1, $2, $3));
					push (@arguments, ($1, $2, $3));
				}
			}
		}

		if ( $line =~ /^\D+\.(.+)\(\)\;/) {
			push (@cosas, $line);
			#push (@cosas, $1);
			push (@arguments, $1);
			my @resultado = fill_with_arguments(\@arguments, $rule_count, \%rules);
			push (@cosas, @resultado);
			@arguments = ();
			$rule_count++;
		}

		
	}

	return \%rules;
}

sub fill_with_arguments {
	my $args_ref = shift;
	my $r_count = shift;
	my $rules = shift;
	my $key_index = 0;
	my @cosa = ();
	#push (@cosa, $r_count);
	my @args = @{$args_ref};

	for my $j (0 .. $#args-1) {
		my $temp = ($j/3)%4;
		my $key = (KEYS)[$key_index];
		${$rules}{"rule".$r_count}{$key.$temp} = $args[$j];
		#push (@cosas, $key);
		push (@cosa, $args[$j]);
		$key_index++;
		if ($key_index == 3) {$key_index = 0;}
		${$rules}{"rule".$r_count}{(KEYS)[3]} = $args[$#args];
	}
	push (@cosa, $args[$#args]);

	return @cosa;
}

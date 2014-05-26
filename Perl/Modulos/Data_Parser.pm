package Data_Parser;

use 5.012;
use warnings;
use File::Slurp qw(read_file);
use Carp qw(croak);
use constant KEYS => qw(
http_reply_code http_method duration_milliseconds content_type_MCT content_type server_or_cache_address time squid_hierarchy bytes url_core url_TLD client_address
);
use base 'Exporter';
our @EXPORT = qw(log_data_to_hash);


sub log_data_to_hash {
	my $file = shift || croak "Please specify a valid Drools file.";

	my $entry_count = 0;
	my @rows = read_file( $file );
	my @cosas = ();	
	my @arguments = ();
	my %data;
	shift @rows;

	for my $line ( @rows ) {
		my @data = split (/;/, $line);

		for my $d (0 .. $#data) {

			if ($data[$d] =~ /"(.+)"\n?/) { 
				$data[$d] = $1;
			}

			if ($d == 3) {
				if ($data[$d] =~ /^(\w+-*\w+)[\/?]\w+/) {
					push (@arguments, $1);
					push (@arguments, $data[$d]);
				} else {
					push (@arguments, $data[$d]);
					push (@arguments, $data[$d]);
				}
			} elsif ($data[$d] =~ /^\d{1,2}\:\d{2}\:\d{2}/) {
				push (@arguments, "\"".$data[$d]."\"");
			} elsif ($data[$d] =~ /(https?:\/\/([-\w\.]+)+(:\d+)?(\/([\w\/\_\-\.]*(\?\S+)?)?)?)/) {
				my @doms = split (/\./, $2);
				push (@arguments, $doms[$#doms-1]);
				push (@arguments, $doms[$#doms]);
			} else {
				push (@arguments, $data[$d]);
			}
		}

		my @result = fill_with_arguments(\@arguments, $entry_count, \%data);
		@arguments = ();
		$entry_count++;
		
	}

	return \%data;
}

sub fill_with_arguments {
	my $args_ref = shift;
	my $e_count = shift;
	my $data_ref = shift;
	my $key_index = 0;
	my @filling = ();
	#push (@cosa, $r_count);
	my @args = @{$args_ref};

	for my $j (0 .. $#args) {
		my $key = (KEYS)[$key_index];
		${$data_ref}{"entry".$e_count}{$key} = $args[$j];
		#push (@cosas, $key);
		push (@filling, $args[$j]);
		$key_index++;
	}

	return @filling;
}

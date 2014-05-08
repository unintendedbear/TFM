package Data_Parser;

use 5.012;
use warnings;
use File::Slurp qw(read_file);
use Carp qw(croak);
use constant KEYS => qw(
http_reply_code http_method duration_milliseconds content_type_MCT content_type server_or_cache_address time squid_hierarchy bytes url_core client_address
);
use Sort::Naturally;

my $file_in = shift || croak "Please specify a valid Log file.";
my $file_out;
my $arff_file;
if ($file_in =~ /(.+)\.csv/) {
	$file_out = "$1_only_requests.csv";
	$arff_file = "$1_only_requests.arff";
}

my @rows = read_file( $file_in );

my $keys = shift @rows;
my @keys = split /;/, $keys;
foreach (@keys) {
	$_ =~ s/(.+)\s?/$1/;
}

#print "@keys\n";

my %log_data;
my $entry_count = 0;

for my $line ( @rows ) {
	my @data = split /;/, $line;

	foreach (@data) {
		$_ =~ s/(.+)\s?/$1/;
	}

	for my $i (0 .. $#keys) {
		
		$log_data{"entrada".$entry_count}{$keys[$i]} = $data[$i];
	}

	$entry_count++;
}

my @entry_index = sort {$log_data{$a} <=> $log_data{$b}} keys %log_data;
@entry_index = nsort(@entry_index);

#for my $j (@claves) {
#	print "-----------$j-----------\n";
#	my @claves2 = sort keys %{ $log_data{$j} };
#	for my $k (@claves2) {
#		print "$k, $log_data{$j}{$k}\n";
#	}
#}

my %cmp_log = %log_data;

for my $index (0 .. $#entry_index) {

	for my $index_cmp (0 .. $#entry_index) {

		#if ($log_data{$entry_index[$index]}{"url"} eq $cmp_log{$entry_index[$index_cmp]}{"url"}) {
		#	print "URL COINCIDENCE\n";
		#	if ($log_data{$entry_index[$index]}{"server_or_cache_address"} eq $cmp_log{$entry_index[$index_cmp]}{"client_address"}) {
		#			print "COINCIDENCE IN $index & $index_cmp\n";
		#		if ($log_data{$entry_index[$index]}{"client_address"} eq $cmp_log{$entry_index[$index_cmp]}{"server_or_cache_address"}) {
		#			print "COINCIDENCE IN $index & $index_cmp\n";
					#print $log_data{$entry_index[$index]}{"url"}."\n";
		#		}
		#	}
		#}

		if ($log_data{$entry_index[$index]}{"squid_hierarchy"} eq "DEFAULT_PARENT") {
		
		}
		
	}
}


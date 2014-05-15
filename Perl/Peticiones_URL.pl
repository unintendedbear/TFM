package Data_Parser;

use 5.012;
use warnings;
use File::Slurp qw(read_file);
use Carp qw(croak);
use constant ARFF_KEYS => qw(
http_reply_code http_method duration_milliseconds content_type_MCT content_type server_or_cache_address time squid_hierarchy bytes url_core client_address etiqueta
);
use constant KEYS => qw(
http_reply_code http_method duration_milliseconds content_type server_or_cache_address time squid_hierarchy bytes url client_address etiqueta
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
my @delete_list = ();
my @already_checked = ();

for my $index (0 .. $#entry_index) {
	next if (grep {$_ == $index} @already_checked);

	for my $index_cmp (0 .. $#entry_index) {
		next if ($index_cmp == $index);
		

		if ($log_data{$entry_index[$index]}{'url'} eq $cmp_log{$entry_index[$index_cmp]}{'url'}) {
			if ($cmp_log{$entry_index[$index_cmp]}{'client_address'} eq $log_data{$entry_index[$index]}{'server_or_cache_address'}) {
				push (@delete_list, $index_cmp);
			} else { push (@already_checked, $index_cmp) unless ($index == $index_cmp-1); }
		}

		
	}
}

foreach (@delete_list) {
	delete $cmp_log{$entry_index[$_]};
}

my @new_index = sort {$cmp_log{$a} <=> $cmp_log{$b}} keys %cmp_log;
@new_index = nsort(@new_index);

my @arff_keys = (ARFF_KEYS)[0 .. 10];

my %respuestas; #http_reply_code
my %metodos;	#http_method
my %ctype;	#content_type
my %serveradd;	#server_or_cache_address
my %squidh;	#squid_hierarchy
my %coredomains;#url
my %clientadd;	#client_address
my %MCTs;	#content_type_MCT
my %etiquetas;	#etiqueta

for my $j (@new_index) {
	
	my @claves2 = sort keys %{ $cmp_log{$j} };
	if ($cmp_log{$j}{'content_type'} =~ /^(\w+-*\w+)[\/?]\w+/) {
		$cmp_log{$j}{'content_type_MCT'} = $1;
	} else {
		$cmp_log{$j}{'content_type_MCT'} = $cmp_log{$j}{'content_type'};
	}
	if ($cmp_log{$j}{'url'} =~ /^(ht|f)tps?:\/\/([\.\-\w]*)\.([\-\w]+)\.(\w+)\/[\/*\w*]*/ || $cmp_log{$j}{'url'} =~ /^(ht|f)tps?:(\/\/)([\-\w]+)\.(\w+)\/[\/*\w*]*/ || $cmp_log{$j}{'url'} =~ /^NONE\:\/\//) {
		$cmp_log{$j}{'url_core'} = $3;
	}
	#delete $cmp_log{$j}{'url'};

	$respuestas{$cmp_log{$j}{'http_reply_code'}}++;
	$metodos{$cmp_log{$j}{'http_method'}}++;
	$ctype{$cmp_log{$j}{'content_type'}}++;
	$serveradd{$cmp_log{$j}{'server_or_cache_address'}}++;
	$squidh{$cmp_log{$j}{'squid_hierarchy'}}++;
	$coredomains{$cmp_log{$j}{'url_core'}}++;
	$clientadd{$cmp_log{$j}{'client_address'}}++;
	$MCTs{$cmp_log{$j}{'content_type_MCT'}}++;
	$etiquetas{$cmp_log{$j}{'etiqueta'}}++;
}

my @respuestas = keys %respuestas;
my @metodos = keys %metodos;
my @ctype = keys %ctype;
my @serveradd = keys %serveradd;
my @squidh = keys %squidh;
my @clientadd = keys %clientadd;
my @coredomains = keys %coredomains;
my @MCTs = keys %MCTs;
my @etiquetas = keys %etiquetas;

my $header=<<EOC;
\@RELATION logsUrl

EOC
  $header .= "\@ATTRIBUTE http_reply_code { ".join(",", @respuestas ).
    " }\n\@ATTRIBUTE http_method { ".join(",", @metodos).
      " }\n\@ATTRIBUTE duration_milliseconds REAL".
	"\n\@ATTRIBUTE content_type_MCT { ".join(",", @MCTs ).
	" }\n\@ATTRIBUTE content_type { ".join(",", @ctype ).
	" }\n\@ATTRIBUTE server_or_cache_address { ".join(",", @serveradd ).
	" }\n\@ATTRIBUTE time DATE \"HH:mm:ss\"".
	"\n\@ATTRIBUTE squid_hierarchy { ".join(",", @squidh ).
	" }\n\@ATTRIBUTE bytes REAL".
	"\n\@ATTRIBUTE url { ".join(",", @coredomains ).
	" }\n\@ATTRIBUTE client_address { ".join(",", @clientadd ).
	" }\n\@ATTRIBUTE label { ".join(",", @etiquetas ).
	" }\n\n\@DATA\n";

my $salida_arff = "$header\n";

for my $entry (@new_index) {

	foreach (@arff_keys) {
		$salida_arff .= $cmp_log{$entry}{$_}.", ";
	}

	$salida_arff .= $cmp_log{$entry}{'etiqueta'}."\n";
}

open (ARFF_OUT, ">$arff_file") or die "No existe el fichero ".$arff_file; #Abrir y leerlo
print ARFF_OUT "$salida_arff\n";
close ARFF_OUT;

my @csv_keys = (KEYS)[0 .. 9];

my $salida_csv = "";

$salida_csv .= join(";", @csv_keys );
$salida_csv .= ";etiqueta\n";

for my $csv_entry (@new_index) {

	foreach (@csv_keys) {
		$salida_csv .= $cmp_log{$csv_entry}{$_}.";";
	}

	$salida_csv .= $cmp_log{$csv_entry}{'etiqueta'}."\n";
}

open (OUT, ">$file_out") or die "No existe el fichero ".$file_out; #Abrir y leerlo
print OUT "$salida_csv\n";
close OUT;

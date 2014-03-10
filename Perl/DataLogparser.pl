#!/usr/local/bin/perl

use warnings;
use strict;
use Data::Dumper;

#my $logfile = "data_100k_instances_url_log_redux.csv"; #Fichero reducido de 50 entradas para pruebas
my $logfile = "data_100k_instances_url_log.csv"; #Fichero de 100k entradas de log
my $arfffile = "salida.arff";
#my $arfffile = "salida2.arff";
my $keysfile = "logkeys.txt";
my %logentradas = (); #Inicializar el hash de entradas de log
my @keys = ();

open (KEYS, "<$keysfile") or die "No existe el fichero ".$keysfile; #Abrir y leerlo

while (<KEYS>) {
	push @keys, split (/\s+/, $_);     #Extraer las claves del fichero logkeys.txt
}

# Claves:
# 0 http_reply_code
# 1 http_method
# 2 duration_milliseconds
# 3 content_type_MCT
# 4 content_type
# 5 server_or_cache_address
# 6 time_hh
# 7 time_mm
# 8 time_ss
# 9 squid_hierarchy
# 10 bytes
# 11 url
# 12 client_address

close KEYS;

my $numentrada = 0;
my $count = 0;

my @rows;

open (IN, "<$logfile") or die "No existe el fichero ".$logfile; #Abrir y leerlo
my @firstrow = split /;/, <IN>; #No necesitamos la primera fila

while (<IN>) {

my @datoslog = split /;/, $_;
my @row = ();

for my $d (0 .. $#datoslog) { 

	if ($datoslog[$d] =~ /"(.+)"/) { 
		$datoslog[$d] = $1;
	}

	if ($d == 3) {
		if ($datoslog[$d] =~ /^(\w+-*\w+)[\/?]\w+/) {
			push @row, $1;
			push @row, $datoslog[$d];
		} else {
			push @row, $1;
			push @row, $datoslog[$d];
		}
	} elsif ($datoslog[$d] =~ /(\w+)\:(\w+)\:(\w+)/) {
		push @row, $1;
		push @row, $2;
		push @row, $3;
	} elsif ($datoslog[$d] =~ /^(ht|f)tps?:\/\/([\.\-\w]*)\.([\-\w]+)\.(\w+)\/[\/*\w*]*/ || $datoslog[$d] =~ /^(ht|f)tps?:(\/\/)([\-\w]+)\.(\w+)\/[\/*\w*]*/) {
		push @row, $3;
	} else { 
		push @row, $datoslog[$d];
	}
}		

	for my $i (0 .. $#keys) {
		
		$logentradas{"entrada".$numentrada}{$keys[$i]} = $row[$i];
	}

	$numentrada++;
	push @rows, \@row;
	
}

close IN;

#print Dumper(\%logentradas);

my %respuestas; #http_reply_code
my %metodos;	#http_method
my %ctype;	#content_type
my %serveradd;	#server_or_cache_address
my %squidh;	#squid_hierarchy
my %coredomains;#url
my %clientadd;	#client_address
my %MCTs;	#content_type_MCT

foreach my $name (sort keys %logentradas) {


	#if ($logentradas{$name}{'url'} =~ /^(ht|f)tps?:\/\/(\w+).(\w+).(\w+)[\/*\w*]/i) {
		#print "$2 - $3 - $4\n";
		#$coredomains{$2}++;
		#$coredomains{$3}++;
		#$coredomains{$4}++;
	#}

	$respuestas{$logentradas{$name}{'http_reply_code'}}++;
	$metodos{$logentradas{$name}{'http_method'}}++;
	$ctype{$logentradas{$name}{'content_type'}}++;
	$serveradd{$logentradas{$name}{'server_or_cache_address'}}++;
	$squidh{$logentradas{$name}{'squid_hierarchy'}}++;
	$coredomains{$logentradas{$name}{'url'}}++;
	$clientadd{$logentradas{$name}{'client_address'}}++;
	$MCTs{$logentradas{$name}{'content_type_MCT'}}++;

}

my @respuestas = keys %respuestas;
my @metodos = keys %metodos;
my @ctype = keys %ctype;
my @serveradd = keys %serveradd;
my @squidh = keys %squidh;
my @clientadd = keys %clientadd;
my @coredomains = keys %coredomains;
my @MCTs = keys %MCTs;

#print Dumper(\%metodos);
#print Dumper(\%respuestas);
#print Dumper(\%MCTs);
#print Dumper(\%coredomains);

# http_reply_code 		CAT
# http_method 			CAT
# duration_milliseconds		REAL
# content_type_MCT 		CAT
# content_type 			CAT
# server_or_cache_address	CAT
# time_hh			REAL
# time_mm			REAL
# time_ss			REAL
# squid_hierarchy		CAT
# bytes				REAL
# url				CAT
# client_address		CAT

my $header=<<EOC;
\@RELATION logsUrl

EOC
  $header .= "\@ATTRIBUTE http_reply_code { ".join(",", @respuestas ).
    " }\n\@ATTRIBUTE http_method { ".join(",", @metodos).
      " }\n\@ATTRIBUTE duration_milliseconds REAL".
	"\n\@ATTRIBUTE content_type_MCT { ".join(",", @MCTs ).
	" }\n\@ATTRIBUTE content_type { ".join(",", @ctype ).
	" }\n\@ATTRIBUTE server_or_cache_address { ".join(",", @serveradd ).
	" }\n\@ATTRIBUTE time_hh REAL".
	"\n\@ATTRIBUTE time_mm REAL".
	"\n\@ATTRIBUTE time_ss REAL".
	"\n\@ATTRIBUTE squid_hierarchy { ".join(",", @squidh ).
	" }\n\@ATTRIBUTE bytes REAL".
	"\n\@ATTRIBUTE url { ".join(",", @coredomains ).
	" }\n\@ATTRIBUTE client_address { ".join(",", @clientadd ).
	" }\n\n\@DATA\n";

my $salida = "$header\n";
for my $r (@rows ) {
	$salida .= join(", ", @$r )."\n";
}

#print "$salida\n";


open (OUT, ">$arfffile") or die "No existe el fichero ".$arfffile; #Abrir y leerlo
print OUT $salida;
close OUT;


#for my $x (0 .. $#leidasurl) {
#	if ($leidasurl[$x+1] - $leidasurl[$x] > 1) {
		#print "$leidasurl[$x]\n";
		#print "....................\n";
		#for my $y (0 .. $#keys) {
		#	print $logentradas{"entrada".$leidasurl[$x+1]}{$keys[$y]};
		#	print "\n";
		#}
#	}
#}

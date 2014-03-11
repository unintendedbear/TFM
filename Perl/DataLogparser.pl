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

# Claves:				Datos:
# 0 http_reply_code			0 http_reply_code
# 1 http_method				1 http_method
# 2 duration_milliseconds		2 duration_milliseconds
# 3 content_type_MCT			3 content_type
# 4 content_type			4 server_or_cache_address
# 5 server_or_cache_address		5 time
# 6 time				6 squid_hierarchy
# 7 squid_hierarchy			7 bytes
# 8 bytes				8 url
# 9 url					9 client_address
# 10 client_address

close KEYS;

my $numentrada = 0;
my $count = 0;

my @rows;

open (IN, "<$logfile") or die "No existe el fichero ".$logfile; #Abrir y leerlo
my @firstrow = split /;/, <IN>; #No necesitamos la primera fila

while (<IN>) {

my @datoslog = split /;/, $_;
my @row = ();

#if ($numentrada == 25838 || $numentrada == 25839 || $numentrada == 25840 || $numentrada == 25841 || $numentrada == 25842 || $numentrada == 25843 || $numentrada == 25844) { print "$numentrada-----@datoslog\n"; }

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
	} elsif ($datoslog[$d] =~ /^\d{2}\:\d{2}\:\d{2}/) {
		push @row, "\"".$datoslog[$d]."\"";
	} elsif ($datoslog[$d] =~ /^(ht|f)tps?:\/\/([\.\-\w]*)\.([\-\w]+)\.(\w+)\/[\/*\w*]*/ || $datoslog[$d] =~ /^(ht|f)tps?:(\/\/)([\-\w]+)\.(\w+)\/[\/*\w*]*/) {
		push @row, $3;
	} else { 
		push @row, $datoslog[$d];
	}
}
	#print "@row\n";		

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
#print Dumper(\%clientadd);

# http_reply_code 		CAT
# http_method 			CAT
# duration_milliseconds		REAL
# content_type_MCT 		CAT
# content_type 			CAT
# server_or_cache_address	CAT
# time				DATE
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
	" }\n\@ATTRIBUTE time DATE \"HH:mm:ss\"".
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


#my $file = "pruebas.txt";
#open (OUT2, ">$file") or die "No existe el fichero ".$file; #Abrir y leerlo

#foreach my $name (sort keys %logentradas) {

#	my $comp = $logentradas{$name}{'client_address'};
#	if ($comp =~ /(\d{1,3})\.(\d{1,3})\.(\d{1,3})\.(\d{1,3})/){print "holas que bien\n";} else {print OUT2 "$name ---- $logentradas{$name}{'client_address'}\n";}

#}


#	print OUT2 Dumper(\%{$logentradas{"entrada25838"}});
#	print OUT2 Dumper(\%{$logentradas{"entrada25839"}});
#	print OUT2 Dumper(\%{$logentradas{"entrada25840"}});
#	print OUT2 Dumper(\%{$logentradas{"entrada25841"}});
#	print OUT2 Dumper(\%{$logentradas{"entrada25842"}});
#	print OUT2 Dumper(\%{$logentradas{"entrada25843"}});
#	print OUT2 Dumper(\%{$logentradas{"entrada10328"}});

#close OUT2;

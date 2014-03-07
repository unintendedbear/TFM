#!/usr/local/bin/perl

use warnings;
use strict;
use Data::Dumper;

#my $logfile = "data_100k_instances_url_log_redux.csv"; #Fichero reducido de 50 entradas para pruebas
my $logfile = "data_100k_instances_url_log.csv"; #Fichero de 100k entradas de log
my $arfffile = "salida.arff";
my %logentradas = (); #Inicializar el hash de entradas de log

open (IN, "<$logfile") or die "No existe el fichero ".$logfile; #Abrir y leerlo

my @keys = split /;/, <IN>;     #Extraer las claves de la primera línea del fichero
for my $k (0 .. $#keys) { 
	$keys[$k] =~ /"(.+)"/;
	$keys[$k] = $1;
}
push (@keys, 'content_type_MCT'); # Entonces content_type_MCT está en $keys[$#keys]

my $numentrada = 0;
my $count = 0;

my @rows;

while (<IN>) {

my @datoslog = split /;/, $_;
for my $d (0 .. $#datoslog) { 
	if ($datoslog[$d] =~ /"(.+)"/) { 
		$datoslog[$d] = $1;
	}
}
	my $urltemp;
	my @row = ();

	

	for my $i (0 .. $#keys-1) {
		$count = $count + $i;
		$logentradas{"entrada".$numentrada}{$keys[$i]} = $datoslog[$i];
		if ($datoslog[$i] =~ /^(\w+-*\w+)[\/?]\w+/) {
			$logentradas{"entrada".$numentrada}{$keys[$#keys]} = $1;
		}
#/^(ht|f)tps?:\/\/((\w*-*\w*-*\w*).?(\w+).?(\w+)).(\w+)\/[\/*\w*]/i || 
# /((([A-Za-z]{3,9}:(?:\/\/)?)(?:[-;:&=\+\$,\w]+@)?[A-Za-z0-9.-]+|(?:www.|[-;:&=\+\$,\w]+@)[A-Za-z0-9.-]+)((?:\/[\+~%\/.\w-_]*)?\??(?:[-\+=&;%@.\w_]*)#?(?:[\w]*))?)/
# /^(ht|f)tps?:\/\/((\w*-*\w*-*\w*).?(\w*-*\w*-*\w*).?(\w*-*\w*-*\w*).?(\w*-*\w*-*\w*))\.(\w+)\/[\/*\w*]/i
		if ($datoslog[$i] =~ /^(ht|f)tps?:\/\/([\.\-\w]*)\.([\-\w]+)\.(\w+)\/[\/*\w*]*/ || $datoslog[$i] =~ /^(ht|f)tps?:(\/\/)([\-\w]+)\.(\w+)\/[\/*\w*]*/) {
			#print "$2 - $3\n";
			$urltemp = $3;
			push @row, $3;
		} elsif ($i == 8){
			$urltemp = $datoslog[$i];
			push @row, $datoslog[$i];
		} else { 
			push @row, $datoslog[$i];
		}
	}
	$logentradas{"entrada".$numentrada}{"url"} = $urltemp;

	if (!$logentradas{"entrada".$numentrada}{$keys[$#keys]}) {
		$logentradas{"entrada".$numentrada}{$keys[$#keys]} = $logentradas{"entrada".$numentrada}{"content_type"};
	}

	push @row, $logentradas{"entrada".$numentrada}{$keys[$#keys]};

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

#http_reply_code, http_method, duration_milliseconds, content_type, server_or_cache_address, time, squid_hierarchy, bytes, url, client_address, content_type_MCT

my $header=<<EOC;
\@RELATION logsUrl

EOC
  $header .= "\@ATTRIBUTE http_reply_code\n{ ".join(",", @respuestas ).
    " }\n\@ATTRIBUTE http_method\n{ ".join(",", @metodos).
      " }\n\@ATTRIBUTE duration_milliseconds REAL".
	"\n\@ATTRIBUTE content_type\n{ ".join(",", @ctype ).
	" }\n\@ATTRIBUTE server_or_cache_address\n{ ".join(",", @serveradd ).
	" }\n\@ATTRIBUTE time REAL".
	"\n\@ATTRIBUTE squid_hierarchy\n{ ".join(",", @squidh ).
	" }\n\@ATTRIBUTE bytes REAL".
	"\n\@ATTRIBUTE url\n{ ".join(",", @coredomains ).
	" }\n\@ATTRIBUTE client_address\n{ ".join(",", @clientadd ).
	" }\n\@ATTRIBUTE content_type_MCT\n{ ".join(",", @MCTs ).
	" }\n\n\@DATA\n";

my $salida = "$header\n";
  for my $r (@rows ) {
    $salida .= join(", ", @$r )."\n";
  }


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

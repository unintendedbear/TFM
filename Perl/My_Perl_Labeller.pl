#!/usr/local/bin/perl

use warnings;
use strict;
use Data::Dumper;

####################################################################################################################################
# Este programa intenta clasificar una serie de datos de log parseados en función de unas reglas en Drools también parseadas.	   #
#																   #
# Como resultado quiero obtener un hash que sea:										   #
#																   #
#%datos_etiquetados = (														   #
#   			entrada0 => deny,											   #
#			entrada1 => deny,											   #
#			entrada2 => allow,											   #
#			...											   		   #
#			entrada99998 => deny,											   #
#			entrada99999 => allow,											   #
#			entrada100000 => deny											   #
#   		      },											   		   #
#);											   					   #
####################################################################################################################################
# Las reglas de políticas son del tipo:
#
#rule "policy-1 FLV"
#when
#	squid:Squid(dif_MCT=="video",dif_content=="video/x-flv")
#then
#	PolicyDecisionPoint.deny();
#end
#
# Del parseo de las reglas obtenemos un hash multidimensional de reglas con el siguiente formato:
#
#%reglas = (
#   regla => {
#       accion	  => deny, 
#       campo0	  => dif_MCT,
#	relacion0 => ==,
#	valor0	  => video
#       campo1	  => dif_content,
#	relacion1 => ==
#	valor1	  => video/x-flv,
#   },
#);
#

my $drlfile = "Initial-rules-squid.drl"; #Este es el fichero de reglas de Drools de Sergio

my %reglas = (); #Inicializar el hash
my @keys = ("accion", "campo", "relacion", "valor");
my $ind_regla = 0;
my $orden = "";


open (IN, "<$drlfile") or die "No existe el fichero ".$drlfile;
while (<IN>)
{
	my @argumentos = ();
	if ($_ =~ /^\D+\.(.+)\(\)\;/) {
		$orden = $1;
		$reglas{"regla".$ind_regla}{$keys[0]} = $orden; # Como la acción se lee después de las condiciones, hay que meterlo en el 
		$ind_regla++;					# hash antes de que pare a la siguiente regla.
	}
	if ($_ =~ /^\D+:\D+\((.+)\)/) {
		for my $i (my @condiciones = split /,/, $1) {
			if ($i =~ /(.*)(==)"(.+)"/ || $i =~ /(.+)([>|<|=])(\d+)/ || $i =~ /(.+) (.+) "\*\.(.+)\.\*"/) {
				my @argtemp = ($1, $2, $3);
				push(@argumentos, @argtemp);
			}
		}	
	}

	my $ind_keys = 1;
	for my $j (0 .. $#argumentos) {
		my $temp = ($j/3)%3;
		$reglas{"regla".$ind_regla}{$keys[$ind_keys].$temp} = $argumentos[$j];
		$ind_keys++;
		if ($ind_keys == 4) {$ind_keys = 1;} 
#		print Dumper(\%reglas); # Para ver como se va rellenando el hash
	}
}
close IN;

# Del parseo de datos obtenemos otro hash multidimensional de formato:
#
#%logentradas = (
#   entrada => {
#       http_reply_code		=> 200, 
#       http_method		=> GET,
#	duration_miliseconds	=> 1114,
#	content_type_MCT	=> application,
#	content_type		=> application/octet-stream,
#       server_or_cache_address	=> 192.168.4.4,
#	time 			=> 08:30:08,
#	squid_hierarchy		=> DEFAULT_PARENT,
#	bytes			=> 106961,
#	url			=> http://...,
#	client_address		=> 10.159.76.30
#   },
#);
#
# NOTA: la última entrada "content_type_MCT" no existe en los logs pero se ha creado por mayor comodidad para etiquetar

#my $logfile = "data_100k_instances_url_log_redux.csv"; #Fichero reducido de 50 entradas para pruebas
my $logfile = "data_100k_instances_url_log.csv"; #Fichero de 100k entradas de log
#my $logfile = "data_2000_instances_url_log.csv"; #Fichero de 2000 entradas de log 

my $keysfile = "logkeys.txt";
my %logentradas = (); #Inicializar el hash de entradas de log
my @keys2 = (); #Inicializar el array de claves 

open (KEYS, "<$keysfile") or die "No existe el fichero ".$keysfile; #Abrir y leerlo

while (<KEYS>) {
	push @keys2, split (/\s+/, $_);     #Extraer las claves del fichero logkeys.txt
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
my @rows;

open (IN2, "<$logfile") or die "No existe el fichero ".$logfile; #Abrir y leerlo
my @firstrow = split /;/, <IN2>; #No necesitamos la primera fila

while (<IN2>) {

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
	} elsif ($datoslog[$d] =~ /^\d{2}\:\d{2}\:\d{2}/) {
		push @row, "\"".$datoslog[$d]."\"";
	} elsif ($datoslog[$d] =~ /^(ht|f)tps?:\/\/([\.\-\w]*)\.([\-\w]+)\.(\w+)\/[\/*\w*]*/ || $datoslog[$d] =~ /^(ht|f)tps?:(\/\/)([\-\w]+)\.(\w+)\/[\/*\w*]*/) {
		push @row, $3;
	} else { 
		push @row, $datoslog[$d];
	}
}
	#print "@row\n";		

	for my $i (0 .. $#keys2) {
		
		$logentradas{"entrada".$numentrada}{$keys2[$i]} = $row[$i];
	}

	$numentrada++;
	push @rows, \@row;
	
}

close IN2;

# Los dos hash:
# print Dumper(\%reglas);
# print Dumper(\%logentradas);

# Hay que tener en cuenta las equivalencias entre terminación de Squid/Drools y el campo que se busca en los datos. Tenemos que:
#
# dif_code	= http_code
# dif_met	= http_method
# dif_MCT	= content_type_MCT
# dif_content	= content_type
# dif_squid	= squid_hierarchy
# url		= url
# bytes		= bytes
#

my %diccionario;
$diccionario{"dif_code"} = "http_code";
$diccionario{"dif_met"} = "http_method";
$diccionario{"dif_MCT"} = "content_type_MCT";
$diccionario{"dif_content"} = "content_type";
$diccionario{"diff_content"} = "content_type_MCT";
$diccionario{"dif_squid"} = "squid_hierarchy";
$diccionario{"url"} = "url";
$diccionario{"bytes"} = "bytes";
$diccionario{"http_method"} = "http_method";

# Así que si la regla dice dif_MCT=="video", tenemos que buscar la clave $diccionario{"dif_MCT"} en el hash de entradas de log y ver si su valor es # "video".

# Para extraer cosas de un hash, hay que saber en qué orden se obtienen cuando se usa 'sort keys'. Obtenemos:
# regla0, accion: deny			# entrada0, bytes: 106961
# regla0, campo0: dif_MCT		# entrada0, client_address: 10.159.76.30
# regla0, campo1: dif_content		# entrada0, content_type: application/octet-stream
# regla0, relacion0: ==			# entrada0, content_type_MCT: application
# regla0, relacion1: ==			# entrada0, duration_milliseconds: 1114
# regla0, valor0: video			# entrada0, http_method: GET
# regla0, valor1: video/x-flv		# entrada0, http_reply_code: 200
					# entrada0, server_or_cache_address: 192.168.4.4
					# entrada0, squid_hierarchy: DEFAULT_PARENT
					# entrada0, time: 08:30:08
					# entrada0, url: http://au.download.windowsupdate.com/msdownload/update/software/uprl/2013/02/wu-windows6.1-kb2729094-v2-x64_7d08944484d693e51abaf9c37ec5b54019309e22.exe
#
# Es decir, por orden alfabético.
#

my $ind_reglas = 0;  # Este índice es para ver cuántas reglas hemos comprobado ya.
my $ind_campos = 1;  # Este índice es para ir sacando las condiciones de la regla, sabiendo que primero obtenemos los dos campos, luego
		 # lo que tienen que cumplir y por último los dos valores.
my $ind_entrada = 0; # Este índice es para saber qué entrada estamos comprobando.

my @total_reglas = sort keys %reglas; # regla0 regla1 regla2 regla3 regla4 regla5 regla6 regla7 regla8
my @total_entradas = sort keys %logentradas; # entrada0 ... entrada999999

my %datos_etiquetados = ();

my $allowed = 0;
my $denied = 0;
my $unlabelled = 0;


#print "Lista\n";
#print "El diccionario: ".Dumper(\%diccionario)."\n";
#print "Los índices \$ind_reglas: $ind_reglas, \$ind_campos: $ind_campos e \$ind_entrada: $ind_entrada\n";
#print "Las reglas: @total_reglas\n y las entradas @total_entradas\n";
#print "Y los datos etiquetados:\n";
#print Dumper(\%datos_etiquetados);

for $ind_reglas (0 .. $#total_reglas) {

	my $etiqueta = $reglas{$total_reglas[$ind_reglas]}{"accion"}; # Aquí sabremos si vamos a etiquetar las entradas como "Deny" o "Allow"

	#print "La acción tomada es: $etiqueta\n";

	my @total_campos = sort keys %{$reglas{$total_reglas[$ind_reglas]}};

	#print "Se toman los campos: @total_campos\n";

	my $salto = $#total_campos/3; # Esto es para tener en cuenta cómo están ordenadas las claves en el hash de reglas.
				      # Es decir, $salto es igual al número de condiciones en una regla.

	#print "Y en la regla $ind_reglas, hay $salto condiciones.\n";

	my @flags = ();
	for my $posicion (0 .. $#total_entradas) { $flags[$posicion] = 0; } # Contadores de condiciones a 0

	#print "El array de flags es @flags\n";

	$ind_campos = 1;

	for $ind_campos (1 .. $salto) {

		#print "El índice de campos vale: $ind_campos\n";

		my $nombre_campo = $reglas{$total_reglas[$ind_reglas]}{$total_campos[$ind_campos]}; #ind_campos = 1...4...7 
		$ind_campos = $ind_campos + $salto;						    #ind_campos = 2...5...8

		#print "El índice de campos aumenta hasta: $ind_campos\n";

		my $relacion = $reglas{$total_reglas[$ind_reglas]}{$total_campos[$ind_campos]};
		$ind_campos = $ind_campos + $salto;						   #ind_campos = 3...6...9

		#print "El índice de campos aumenta hasta: $ind_campos\n";

		my $nombre_valor = $reglas{$total_reglas[$ind_reglas]}{$total_campos[$ind_campos]};
		$ind_campos++;									   #ind_campos = 4...7...10

		#print "El índice de campos aumenta hasta: $ind_campos\n";

		# Ya tenemos la condición extraída: $nombre_campo tiene que ser $relacion a $nombre_valor
		# Ahora obtenemos la traducción.

		my $nombre_campo2 = $diccionario{$nombre_campo}; # Obtenemos la clave para buscar en el hash de logs

		#print "Obtenemos la condición $nombre_campo - $relacion - $nombre_valor. Y \$nombre_campo2: $nombre_campo2\n";
		#print "Relacion $relacion\n";

		for $ind_entrada (0 .. $#total_entradas) {

			my $nombre_valor2 = $logentradas{$total_entradas[$ind_entrada]}{$nombre_campo2}; # Este es el valor que hay comparar

			#print "$ind_entrada :::: El valor de $nombre_campo2 es $nombre_valor2\n";
			#print "Comparamos $nombre_valor con $nombre_valor2\n";

			# Hay que tener en cuenta que $relación es un string y que hay que realizar distintas operaciones
			# Si la condición se cumple, se aumentará el flag en 1 para comprobar al final si se cumplen todas las condiciones para que 				# se aplique la regla y así etiquetar la entrada.

			if ($relacion =~ /==/) {
				if ($nombre_valor eq $nombre_valor2) { $flags[$ind_entrada]++; }
			}
			if ($relacion =~ />/) {
				if ($nombre_valor2 > $nombre_valor) { $flags[$ind_entrada]++; }
			}
			if ($relacion =~ /</)  {
				if ($nombre_valor2 < $nombre_valor) { $flags[$ind_entrada]++; }
			}
			if ($relacion =~ /matches/) {
				if ($nombre_valor2 =~ m/$nombre_valor/) {
					#print "Encontrado $nombre_valor\n";
					$flags[$ind_entrada]++;
				} else { if ($nombre_valor eq $nombre_valor2) { print "$nombre_valor\n"; $flags[$ind_entrada]++; } }
			}
			
		}
	}

	# Ahora miramos qué entradas cumplen las condiciones para aplicarle la etiqueta

	for my $temp (0 .. $#flags) {
		#print "$flags[$temp] con salto $salto\t";
		if ($flags[$temp] >= $salto) { # Se compara con $salto porque $salto es igual al número de condiciones que se tienen que dar para 						       # que se aplique la regla. Cada posición en @flags que sea igual al número de condiciones, será una 						       # entrada que se pueda etiquetar.

			$datos_etiquetados{$total_entradas[$temp]} = $etiqueta;
			$logentradas{$total_entradas[$temp]}{"etiqueta"} = $etiqueta;
			if ($etiqueta =~ /allow/) { $allowed++; }
			if ($etiqueta =~ /deny/) { $denied++; }
			#print "\n\n------------------ $etiqueta ------------------------\n\n";
			#print Dumper(\%{$logentradas{$total_entradas[$temp]}});
		}

		# Etiquetamos con "no label" las entradas que no hayan sido etiquetadas con "allow" ni con "deny"

		if (!$datos_etiquetados{$total_entradas[$temp]}) {
			$datos_etiquetados{$total_entradas[$temp]} = "no_label";
			$logentradas{$total_entradas[$temp]}{"etiqueta"} = "no_label";
			$unlabelled++;
		}
	}

}

#print Dumper(\%datos_etiquetados);
#print Dumper(\%logentradas);
print "En total hay $allowed allow, $denied deny, y $unlabelled sin etiqueta\n";


#################################################
# WEKA
#################################################

#my $arfffile = "salida.arff";  #100k
#my $arfffile = "salida2.arff"; #redux (32~)
#my $arfffile = "salida3.arff"; #100
my $arfffile = "data_100k_instances_url_log.arff";

my %respuestas; #http_reply_code
my %metodos;	#http_method
my %ctype;	#content_type
my %serveradd;	#server_or_cache_address
my %squidh;	#squid_hierarchy
my %coredomains;#url
my %clientadd;	#client_address
my %MCTs;	#content_type_MCT
my %etiquetas;	#etiqueta

foreach my $name (sort keys %logentradas) {

	$respuestas{$logentradas{$name}{'http_reply_code'}}++;
	$metodos{$logentradas{$name}{'http_method'}}++;
	$ctype{$logentradas{$name}{'content_type'}}++;
	$serveradd{$logentradas{$name}{'server_or_cache_address'}}++;
	$squidh{$logentradas{$name}{'squid_hierarchy'}}++;
	$coredomains{$logentradas{$name}{'url'}}++;
	$clientadd{$logentradas{$name}{'client_address'}}++;
	$MCTs{$logentradas{$name}{'content_type_MCT'}}++;
	$etiquetas{$logentradas{$name}{'etiqueta'}}++;

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
# label				CAT

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

my $salida = "$header\n";
my $count = 0;
for my $r (@rows ) {
	$salida .= join(", ", @$r ).", ".$logentradas{$total_entradas[$count]}{"etiqueta"}."\n";
	$count++;
}

#print "$salida\n";


open (OUT, ">$arfffile") or die "No existe el fichero ".$arfffile; #Abrir y leerlo
print OUT $salida;
close OUT;


#################################################
# CSV
#################################################

#my $label_logfile = "data_2000_instances_url_log_w_labels.csv";
my $label_logfile = "data_100k_instances_url_log_w_labels.csv";

open (INCSV, "<$logfile") or die "No existe el fichero ".$logfile; #Abrir y leerlo
open (OUTCSV, ">$label_logfile") or die "No existe el fichero ".$label_logfile; #Abrir y leerlo

my @row1 = split /;/, <INCSV>;

for my $u (0 .. $#row1) { 
	if ($row1[$u] =~ /"(.+)"/) { 
		$row1[$u] = "\"$1"."\"";
	}
}

push @row1, "\"etiqueta\"";
print OUTCSV "".join(";", @row1)."\n";
print "@row1\n";
my $rowwhat = 0;

while (<INCSV>) {
	my @row2 = split /;/, $_;

	for my $w (0 .. $#row2) { 
		if ($row2[$w] =~ /"(.+)"/) { 
			$row2[$w] = "\"$1"."\"";
		}
	}

	push @row2, "\"".$logentradas{$total_entradas[$rowwhat]}{"etiqueta"}."\"";
	print OUTCSV "".join(";", @row2)."\n";
	$rowwhat++;
}

close INCSV;
close OUTCSV;

update data_resource set display_name=name;

--tidy known names
update data_resource set display_name="University of Tennessee Mammals Collection" where display_name like "(UTC) University of Tennessee Mammals Collection";
update data_resource set display_name="University of Tennessee - Molluscs Collection" where display_name like "(UTC-MO) University of Tennessee - Molluscs Collection";
update data_resource set display_name="University of Tennessee - Amphibians Collection" where display_name like "(UTCA) University of Tennessee - Amphibians Collection";
update data_resource set display_name="University of Tennessee - Birds Collection" where display_name like "(UTCB) University of Tennessee - Birds Collection";
update data_resource set display_name="University of Tennessee - Fungi Collection" where display_name like "(UTCF) University of Tennessee - Fungi Collection";
update data_resource set display_name="University of Tennessee - Fossil Birds Collection" where display_name like "(UTCFB) University of Tennessee - Fossil Birds Collection";
update data_resource set display_name="University of Tennessee - Fossil Mammals Collection" where display_name like "(UTCFM) University of Tennessee - Fossil Mammals Collection";
update data_resource set display_name="University of Tennessee - Mammals Collection" where display_name like "(UTCM) University of Tennessee - Mammals Collection";
update data_resource set display_name="University of Tennessee - Reptiles Collection" where display_name like "(UTCR) University of Tennessee - Reptiles Collection";
update data_resource set display_name="Zoological Museum Amsterdam - Amphibia and Reptilia" where display_name like "(Zoological Museum Amsterdam) Amphibia and Reptilia";
update data_resource set display_name="Zoological Museum Amsterdam - Aves" where display_name like "(Zoological Museum Amsterdam) Aves";
update data_resource set display_name="Zoological Museum Amsterdam - Bombidae" where display_name like "(Zoological Museum Amsterdam) Bombidae";
update data_resource set display_name="Zoological Museum Amsterdam - Bryozoa" where display_name like "(Zoological Museum Amsterdam) Bryozoa";
update data_resource set display_name="Zoological Museum Amsterdam - Coelenterata" where display_name like "(Zoological Museum Amsterdam) Coelenterata";
update data_resource set display_name="Zoological Museum Amsterdam - Coleoptera" where display_name like "(Zoological Museum Amsterdam) Coleoptera";
update data_resource set display_name="Zoological Museum Amsterdam - Diptera" where display_name like "(Zoological Museum Amsterdam) Diptera";
update data_resource set display_name="Zoological Museum Amsterdam - Porifera" where display_name like "(Zoological Museum Amsterdam) Porifera";
update data_resource set display_name="Zoological Museum Amsterdam - Noordzee" where display_name like "(Zoological Museum Amsterdam) Noordzee";
update data_resource set display_name="South Western Pacific Regional OBIS Data provider for the NIWA Marine Biodata Information System" where display_name like "``South Western Pacific Regional OBIS Data provider for the NIWA Marine Biodata Information System``";
update data_resource set display_name="South Western Pacific Regional OBIS Data All Sea Bio Subset (South Western Pacific OBIS)" where display_name like "``South Western Pacific Regional OBIS Data All Sea Bio Subset`` (South Western Pacific OBIS)";
update data_resource set display_name="South Western Pacific Regional OBIS Data Asteroid Subset (South Western Pacific OBIS)" where display_name like "``South Western Pacific Regional OBIS Data Asteroid Subset`` (South Western Pacific OBIS)";
update data_resource set display_name="South Western Pacific Regional OBIS Data Bryozoan Subset (South Western Pacific OBIS)" where display_name like "``South Western Pacific Regional OBIS Data Bryozoan Subset`` (South Western Pacific OBIS)";
update data_resource set display_name="South Western Pacific Regional OBIS Data Bio Ross Subset (South Western Pacific OBIS)" where display_name like "```South Western Pacific Regional OBIS Data Bio Ross Subset`` (South Western Pacific OBIS)";
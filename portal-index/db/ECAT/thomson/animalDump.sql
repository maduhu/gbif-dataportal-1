select
    tn.canonical as name,
    tn.author as author,
    kn.canonical as k,
    pn.canonical as p,
    cn.canonical as c,
    orn.canonical as o,
    fn.canonical as f,
    gn.canonical as g,
    sn.canonical as s
into outfile '/tmp/Thomson.txt'    
from
    taxon_concept tc
        inner join taxon_name tn on tc.taxon_name_id = tn.id
        left join taxon_concept kc on tc.kingdom_concept_id = kc.id
        left join taxon_concept pc on tc.phylum_concept_id = pc.id
        left join taxon_concept cc on tc.class_concept_id = cc.id
        left join taxon_concept oc on tc.order_concept_id = oc.id
        left join taxon_concept fc on tc.family_concept_id = fc.id
        left join taxon_concept gc on tc.genus_concept_id = gc.id
        left join taxon_concept sc on tc.species_concept_id = sc.id
        left join taxon_name kn on kc.taxon_name_id = kn.id
        left join taxon_name pn on pc.taxon_name_id = pn.id
        left join taxon_name cn on cc.taxon_name_id = cn.id
        left join taxon_name orn on oc.taxon_name_id = orn.id
        left join taxon_name fn on fc.taxon_name_id = fn.id
        left join taxon_name gn on gc.taxon_name_id = gn.id
        left join taxon_name sn on sc.taxon_name_id = sn.id
where 
	tc.data_resource_id=1 and 
	tc.kingdom_concept_id=13140803;
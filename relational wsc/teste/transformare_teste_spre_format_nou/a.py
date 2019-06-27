import xmltodict
import xml.etree.ElementTree as ET
import os

def parcurgere(concept_node):
    concepts = concept_node.findall('concept')
    instances = concept_node.findall('instance')
    for instance in instances:
        inst_to_conc[instance.attrib['name']] = concept_node.attrib['name']
    for concept in concepts:
        if 'name' in concept_node.attrib:
            ierarhie_conc[concept.attrib['name']] = concept_node.attrib['name']
        else:
            ierarhie_conc[concept.attrib['name']] = concept.attrib['name']
        parcurgere(concept)

def taxonomy_transf(old_file, new_file):
    with open(old_file) as fd:
        taxonomy = ET.parse(old_file)
        root = taxonomy.getroot()
        parcurgere(root)
    with open(new_file, "w+") as fd:
        fd.write(str(len(ierarhie_conc.keys())) + ' 0 0\n\n');
        for k,v in ierarhie_conc.items():
            fd.write(str(k)+' '+str(v)+'\n')
        fd.write('\n\n\n')

def problem_transf_to_query(pr_file, query_file):
    with open(pr_file) as fd:
        with open(query_file,"w+") as q_fd:
            pb = ET.parse(pr_file)
            task = pb.getroot().find('task')
            prov = task.find('provided')
            q_fd.write('nume_rand\n')
            nr=0
            for instance in prov.findall('instance'):
                conc = inst_to_conc[instance.attrib['name']]
                q_fd.write(conc+' Param'+instance.attrib['name']+str(nr)+' ')
                nr+=1
            q_fd.write('\n')

            wan = task.find('wanted')
            for instance in wan.findall('instance'):
                conc = inst_to_conc[instance.attrib['name']]
                q_fd.write(conc+' Param'+instance.attrib['name']+str(nr)+' ')
                nr+=1
            q_fd.write('\n\n\n\n')

def services_transf_to_repository(s_file, rep_file):
    with open(s_file) as sfd:
        with open(rep_file, "w+") as r_fd:
            services = ET.parse(sfd).getroot().findall('service')
            r_fd.write(str(len(services))+'\n')
            for service in services:
                r_fd.write(service.attrib['name']+'\n')
                nr=0
                for inp_inst in service.find('inputs').findall('instance'):
                    conc = inst_to_conc[inp_inst.attrib['name']]
                    r_fd.write(conc+' nume_'+inp_inst.attrib['name']+str(nr)+' ')
                    nr+=1
                r_fd.write('\n')
                for out_inst in service.find('outputs').findall('instance'):
                    conc = inst_to_conc[out_inst.attrib['name']]
                    r_fd.write(conc+' nume_'+out_inst.attrib['name']+str(nr)+' ')
                    nr+=1
                r_fd.write('\n\n\n')

for t in range(1, 9):
    inst_to_conc = {}
    ierarhie_conc = {}
    os.makedirs('0'+str(t), exist_ok=True)
    taxonomy_transf('../wsc08/0'+str(t)+'/taxonomy.xml', '0'+str(t)+'/ontology.txt')
    problem_transf_to_query('../wsc08/0'+str(t)+'/problem.xml', '0'+str(t)+'/query.txt')
    services_transf_to_repository('../wsc08/0'+str(t)+'/services.xml', '0'+str(t)+'/repository.txt')

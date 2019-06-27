#include <stdio.h>
#include <string>
#include <vector>
#include <algorithm>
#include <ctime>
#include <unordered_map>
#include <map>

#include "afisare.cpp"
//#include "service_manager.cpp"

using namespace std;



//variabile        ........................................
Cst cst;
Concept_Manager concept_manager;
Service_Manager service_manager;
Relatii_Manager relatii_manager;
vector <vector <Concept>> informatii_stiute_pe_nivele;
vector <vector <Relatie>> relatii_stiute_pe_nivele;
vector <vector <Relatie>> relatii_input_pe_nivele;
map<pair<Concept, Concept>, vector<Relatie>> relatii_input, relatii_in_out;

map<Concept, vector<Relatie>> rel_cu_conc_in_stg, rel_cu_conc_in_dr;

vector<Concept> genereaza_inf_noi_concepte(int nr_concepte, bool noise=false){
    vector<Concept> concepte_noi;
    for (int i=1;i<=nr_concepte;i++){
        Concept c = concept_manager.genereaza_concept();
        c.noise = noise;
        concepte_noi.push_back(c);
    }
    return concepte_noi;
}

void afiseaza_concepte(vector<Concept> concepte){
    for (auto concept : concepte){
        printf("%s  ", concept.nume.c_str());
    }
    printf("\n\n");
}

void genereaza_compozitie_fara_inferente_fara_subtipuri(){
    //genereaza informatii stiute
    for (int i=0;i<cst.nr_nivele;i++){
        vector<Concept> concepte_noi = genereaza_inf_noi_concepte(cst.nr_concepte); //aici se poate gen rand nr de concepte intr-un interval
        concept_manager.concepte.insert(concept_manager.concepte.end(),concepte_noi.begin(),concepte_noi.end());
        //afiseaza_concepte(concepte_noi);
        informatii_stiute_pe_nivele.push_back(concepte_noi);
        vector<Relatie> relatii_noi_input = relatii_manager.genereaza_relatii_noi(concepte_noi, cst.nr_relatii_noi_input);
        relatii_input_pe_nivele.push_back(relatii_noi_input);

        for (auto it=relatii_noi_input.begin();it!=relatii_noi_input.end();it++){
            Relatie r = *it;
            relatii_input[make_pair(r.Ta, r.Tb)].push_back(r);
        }

        if (i>0){
            informatii_stiute_pe_nivele[i].insert(informatii_stiute_pe_nivele[i].end(), informatii_stiute_pe_nivele[i-1].begin(), informatii_stiute_pe_nivele[i-1].end() );
            vector<Relatie> relatii_noi = relatii_manager.genereaza_relatii_noi(informatii_stiute_pe_nivele[i-1], concepte_noi, cst.nr_relatii_noi);
            relatii_stiute_pe_nivele.push_back(relatii_noi);

            for (auto it = relatii_noi.begin(); it!=relatii_noi.end(); it++){
                Relatie r =*it;
                relatii_in_out[make_pair(r.Ta, r.Tb)].push_back(r);
            }
        }else
            relatii_stiute_pe_nivele.push_back(relatii_noi_input);
    }

    //genereaza servicii
    for (int niv = 1; niv<cst.nr_nivele; niv++){
        vector <Serviciu> nivel;
        for (int j=1;j<=cst.nr_serv_pe_nivel;j++){ //aici se poate face rand la nr serv pe nivel
            Serviciu serviciu = service_manager.genereaza_serviciu(informatii_stiute_pe_nivele[niv-1],
                                                               informatii_stiute_pe_nivele[niv],
                                                               cst.nr_param_input, cst.nr_param_output,
                                                               relatii_input,
                                                               relatii_in_out,
                                                               cst.nr_relatii_inp_serv, cst.nr_relatii_out_serv); //aici rand la nr param input si nr param output

            nivel.push_back(serviciu); // asta nu-l folosesc nicaieri..mhm
            service_manager.servicii_totale.push_back(serviciu);
        }
    }
}

void genereaza_noise(){
    vector<Concept> concepte = genereaza_inf_noi_concepte(cst.nr_concepte, true);
    //concept_manager.concepte.insert(concept_manager.concepte.begin(),concepte.begin(),concepte.end()); //pot comenta asta?
    concept_manager.concepte.insert(concept_manager.concepte.end(),concepte.begin(),concepte.end());

    map<pair<Concept, Concept>, vector<Relatie>> pos_relatii;
    vector<Relatie>relatii = relatii_manager.genereaza_relatii_noi(concepte, cst.nr_relatii_irelevante);
    for (auto it=relatii.begin();it!=relatii.end();it++){
        Relatie r =*it;
        pos_relatii[make_pair(r.Ta, r.Tb)].push_back(r);
    }
    for (int i=1;i<=cst.nr_servicii_irelevante;i++){
        Serviciu serviciu = service_manager.genereaza_serviciu(concepte, concepte, cst.nr_param_input, cst.nr_param_output,
                                                               pos_relatii, pos_relatii, cst.nr_relatii, cst.nr_relatii);
        service_manager.servicii_totale.push_back(serviciu);
    }
}

Relatie da_relatie(Concept a, Concept b, map<pair<Concept,Concept>,vector<Relatie>> relatii){
    Relatie r;
    r.nume="NULL";
    if (relatii.count(make_pair(a,b)) <2)// relatii.end())
        return r;
    vector<Relatie> rel_a_b = relatii[make_pair(a,b)];
    return rel_a_b[rand()%(rel_a_b.size()-1)];
}

vector<Regula2> genereaza_reguli2(){
    vector<Regula2> reguli;
    int nr_serv_de_cautat_rel = 10, nr_serv_de_mod = service_manager.servicii_totale.size()/3+1;
    Relatie rel_speciala;
    rel_speciala.nume ="Rel_speciala";
    int ind_reg=1;
    for (int i=1;i<=nr_serv_de_cautat_rel;i++){
        int poz = rand()%(service_manager.servicii_totale.size()-1);
        Serviciu serv = service_manager.servicii_totale[poz];
        if (serv.relatii.size()==0)
            continue;
        Relatie r = serv.relatii[rand()%(serv.relatii.size())];

        Regula2 reg;
        reg.nume = "Reg"+to_string(ind_reg);
        ind_reg++;
        reg.r1 = r;
        reg.r2 = reg.r1;
        reg.r2.nume = rel_speciala.nume;
        reguli.push_back(reg);
    }

    for (int i=1;i<=nr_serv_de_mod;i++){
        int poz = rand()%(service_manager.servicii_totale.size()-1);
        Serviciu serv = service_manager.servicii_totale[poz];
        if (serv.input_param.size()<2)
            continue;
        Parametru I1=serv.input_param[0], I2=serv.input_param[1];
        Relatie r;
        r.Ta = I1.tip; r.Tb = I2.tip;
        r.a = I1.nume; r.b = I2.nume;
        r.nume = rel_speciala.nume;

        service_manager.servicii_totale.erase(service_manager.servicii_totale.begin()+poz);
        serv.relatii.push_back(r);
        service_manager.servicii_totale.push_back(serv);
    }

    relatii_stiute_pe_nivele[0].push_back(rel_speciala);
    return reguli;
}

vector<Regula> genereaza_reguli(){
    vector <Regula> reguli;
    vector <string> nume_fol;
    U u;

    for (auto it = relatii_stiute_pe_nivele.begin();it!=relatii_stiute_pe_nivele.end();it++)
        for (auto it2=(*it).begin();it2!=(*it).end();it2++){
            Relatie r = *it2;
            rel_cu_conc_in_stg[r.Ta].push_back(r);
            rel_cu_conc_in_dr[r.Tb].push_back(r);
        }

    vector <Concept> concepte_bune;
    for (auto it = concept_manager.concepte.begin();it!=concept_manager.concepte.end();it++){
        Concept c = *it;
        if ((rel_cu_conc_in_dr[c].size()>1) &&(rel_cu_conc_in_stg[c].size()>1))
            concepte_bune.push_back(c);
    }
    if (concepte_bune.size()<2)
        return reguli;
   for (int i=1;i<=cst.nr_reguli;i++){
        int x = concept_manager.concepte.size();
        Concept b = concepte_bune[rand()%(concepte_bune.size()-1)];

        Relatie rel_b_c = rel_cu_conc_in_stg[b][rand()%(rel_cu_conc_in_stg[b].size()-1)];
        Relatie rel_a_b = rel_cu_conc_in_dr[b][rand()%(rel_cu_conc_in_dr[b].size()-1)];

        Concept a =rel_a_b.Ta, c = rel_b_c.Tb;

        if (relatii_in_out.find(make_pair(a,c))==relatii_in_out.end())
            continue;

        vector<Relatie> relatii_intre_a_c = relatii_in_out[make_pair(a,c)];
        if (relatii_intre_a_c.size()<2)
            continue;
        Relatie rel_a_c = relatii_intre_a_c[rand()%(relatii_intre_a_c.size()-1)];

        Regula reg;
        reg.nume = u.genereaza_nume(nume_fol,"Reg_");
        reg.r1 = rel_a_b; reg.r2 = rel_b_c; reg.r3 = rel_a_c;
        reguli.push_back(reg);
    }
    return reguli;
}

int main()
{

    cst.citeste();

    srand(std::time(nullptr));
    genereaza_compozitie_fara_inferente_fara_subtipuri();
    vector <Serviciu> repository = service_manager.servicii_totale;
    for (auto it = repository.begin(); it!= repository.end(); it++){
        Serviciu serv = *it;
        printf("SERVICIU: \n");
        printf("%s",serv.toString().c_str());
        printf("\n _________________________________________________________________________\n");
    }

    vector<Regula2> reguli = genereaza_reguli2();

    genereaza_noise();

    repository = service_manager.servicii_totale;

    A a;
    a.afiseaza_servicii(repository);
    a.afiseaza_ontologia(repository, relatii_stiute_pe_nivele, reguli, informatii_stiute_pe_nivele);
    a.afiseaza_query(informatii_stiute_pe_nivele, relatii_input_pe_nivele, relatii_stiute_pe_nivele);
    return 0;
}

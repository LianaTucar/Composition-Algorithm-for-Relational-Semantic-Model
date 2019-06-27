#include "managers.cpp"
#include <stdio.h>

struct A{
    U u;

    void afiseaza_servicii(vector<Serviciu> repository){
        freopen("repository.txt", "w", stdout);
        printf("%d\n",repository.size());
        for (auto it =repository.begin();it!=repository.end();it++){
            Serviciu s = *it;
            printf("%s\n\n",s.toString2().c_str());
        }
    }

    vector<Concept> asigura_toate_conceptele_in_ierarhie(vector <vector <Concept>> informatii_stiute_pe_nivele, vector<Concept> concepte){
        for (auto it = informatii_stiute_pe_nivele.begin(); it!=informatii_stiute_pe_nivele.end();it++){
            vector <Concept> niv = *it;
            for (auto itc = niv.begin(); itc!=niv.end(); itc++){
                Concept c = *itc;
                if (find(concepte.begin(), concepte.end(), c) == concepte.end())
                    concepte.push_back(c);
            }
        }
        return concepte;
    }


    bool contine(Vector<Relatie> rels, Relatie r){
        for (auto it = rels.begin(); it!=rels.end(); it++)
            if ((*it).nume == r.nume)
                return true;
        return false;
    }

    vector<Relatie> reunesteToateRelatiile(vector <vector <Relatie>> relatii, vector<Serviciu> repository){
         vector<Relatie> rez = new Vector();
         for (auto it = relatii.begin();it!=relatii.end();it++){
            for (auto it2=(*it).begin();it2!=(*it).end();it2++){
                Relatie r= (*it2);
                if (!contine(rez, r))
                    rez.push_back(r);
            }

        for (auto it = repository.begin(); it!=repository.end();it++){
            Serviciu s = *it;
            for (auto it2 = s.relatii.begin();it2!=s.relatii.end();it2++)
                if (!contine(rez, (*it2)))
                    rez.push_back(*it2);
        }
        return rez;
    }

    void afiseaza_ontologia(vector<Serviciu> repository, vector <vector <Relatie>> relatii, vector<Regula2> reguli,
                            vector <vector <Concept>> informatii_stiute_pe_nivele){
        freopen("ontology.txt", "w", stdout);
        set<Concept> concepte;
        //reuniune concepte
        for (auto it =repository.begin();it!=repository.end();it++){
            Serviciu s = *it;
            for (auto it2 = s.input_param.begin(); it2!=s.input_param.end();it2++){
                Parametru p =*it2;
                concepte.insert(p.tip);
            }

            for (auto it2 = s.output_param.begin(); it2!=s.output_param.end();it2++){
                Parametru p =*it2;
                concepte.insert(p.tip);
            }
        }


        //numarare_relatii
        vector<Realtie> toateRelatiile - reunesteToateRelatiile(relatii, repository);

        //afisare ierarhie
        vector<Concept> v_concepte;
        v_concepte.assign(concepte.begin(),concepte.end());

        v_concepte = asigura_toate_conceptele_in_ierarhie(informatii_stiute_pe_nivele, v_concepte);
        //afisare numere
        printf("%d %d %d\n\n",v_concepte.size(), toateRelatiile.size(), reguli.size());


        /* am rescris asta dedesupt
        printf("%s %s\n",v_concepte[0].nume.c_str(),v_concepte[0].nume.c_str());
        bool gasit_noise;
        for (int poz=1; poz<v_concepte.size();poz++){ // ce dracu se intampla in forul asta??? :O
            int poza;
            while (1){
                poza = rand()%poz;
                if (v_concepte[poz].noise){
                    if (!gasit_noise){
                        poza=poz;
                        break;
                    }
                    else{
                        gasit_noise=true;
                        if (v_concepte[poza].noise)
                            break;
                    }
                }else
                    if (!v_concepte[poza].noise)
                        break;
            }
            printf("%s %s\n",v_concepte[poz].nume.c_str(),v_concepte[poza].nume.c_str());
        }
        printf("\n");
        */

        printf("%s %s\n",v_concepte[0].nume.c_str(),v_concepte[0].nume.c_str());
        for (int poz=1; poz<v_concepte.size();poz++){
            int poza = rand()%poz;
            printf("%s %s\n",v_concepte[poz].nume.c_str(),v_concepte[poza].nume.c_str());
        }
        printf("\n");

        /*//afisare relatii
        for (auto it = relatii.begin();it!=relatii.end();it++){
            for (auto it2=(*it).begin();it2!=(*it).end();it2++){
                Relatie r= (*it2);
                printf("%s\n",r.nume.c_str());
            }
        }*/

        //afisare relatii
        for (auto it = toateRelatiile.begin(); it!=toateRelatiile.end(); it++){
            Relatie r = (*it);
            printf("%s\n",r.nume.c_str());
        }
        printf("\n");

        //afisare_reguli
        /*for (auto it=reguli.begin();it!=reguli.end();it++){
            Regula r = *it;
            printf("%s X Y Z\n",r.nume.c_str());
            printf("%s X Y %s Y Z\n", r.r1.nume.c_str(), r.r2.nume.c_str());
            printf("%s X Z\n\n", r.r3.nume.c_str());
        }*/
        for (auto it=reguli.begin();it!=reguli.end();it++){
            Regula2 r = *it;
            printf("%s X Y\n",r.nume.c_str());
            printf("%s X Y\n", r.r1.nume.c_str());
            printf("%s X Y\n\n", r.r2.nume.c_str());
        }
        printf("\n");
    }


    pair<string,string> string_cu_relatii(vector<Relatie> relatii, vector<Concept> concepte, int nr_rel_max, int nr_conc_max){
        string str_param, str_rel;
        vector<string> nume_folosite;
        map<string, string> numeConcept_to_numeParametru;

        int j=1;
        for (auto it = concepte.begin(); it!=concepte.end();it++){
            j++;
            if (j==nr_conc_max)
                break;
            numeConcept_to_numeParametru[(*it).nume] = u.genereaza_nume(nume_folosite,"Param_");
        }

        for (auto it = numeConcept_to_numeParametru.begin();it!=numeConcept_to_numeParametru.end(); it++)
            str_param+=(*it).first+" "+(*it).second+" ";

        int i=1;
        for (auto it = relatii.begin();it!=relatii.end();it++,i++){
            if(i>=nr_rel_max)
                break;
            Relatie r = *it;
            if ((numeConcept_to_numeParametru.find(r.Ta.nume)==numeConcept_to_numeParametru.end()) || ((numeConcept_to_numeParametru.find(r.Tb.nume)==numeConcept_to_numeParametru.end())))
                continue;
            string nume_param_a = numeConcept_to_numeParametru[r.Ta.nume];
            string nume_param_b = numeConcept_to_numeParametru[r.Tb.nume];
            str_rel+=r.nume+" "+nume_param_a+" "+nume_param_b+" ";
        }
        return make_pair(str_param,str_rel);
    }

    void afiseaza_query(vector <vector <Concept>> informatii_stiute_pe_nivele,
                        vector <vector <Relatie>> relatii_input_pe_nivele,
                        vector <vector <Relatie>> relatii_stiute_pe_nivele){
        Cst cst;
        freopen("query.txt", "w", stdout);
        printf("nume_rand\n");
        pair<string, string> param_rel_inp = string_cu_relatii(relatii_input_pe_nivele[0], informatii_stiute_pe_nivele[0], cst.query_nr_rel_inp, cst.query_nr_param_max_inp);
        int last_lvl = relatii_input_pe_nivele.size()-1;
        if (informatii_stiute_pe_nivele.size()!=relatii_stiute_pe_nivele.size())
            printf("CIUDAT");
        pair<string, string> param_rel_out = string_cu_relatii(relatii_stiute_pe_nivele[last_lvl], informatii_stiute_pe_nivele[last_lvl], cst.query_nr_rel_out, cst.query_nr_param_max_out);

        string str_query = param_rel_inp.first + "\n" + param_rel_out.first + "\n" + param_rel_inp.second+param_rel_out.second;

        printf("%s\n\n",str_query.c_str());
    }
};

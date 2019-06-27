#include "utilitati.cpp"
#include "relatii.cpp"

#include <algorithm>
#include <utility>
#include <map>

using namespace std;



struct Concept_Manager{
    vector<Concept> concepte;
    vector<string> nume_conc_folosite;
    U u;
    Concept genereaza_concept(){
        string nume;
        Concept c; //obiectu asta o sa se distruga dupa ce ies din functie???????
        c.nume = u.genereaza_nume(nume_conc_folosite, "Concept_");
        return c;
    }
};


struct Service_Manager{
    vector <Serviciu> servicii_totale;
    map<Concept, Concept> dda;
    vector<string> nume_folosite;
    U u;

    Serviciu genereaza_serviciu(vector<Concept> pos_param_input, vector<Concept> pos_param_output, int nr_param_input, int nr_param_output,
                                map<pair<Concept, Concept>, vector<Relatie>> pos_relatii_input,
                                map<pair<Concept, Concept>, vector<Relatie>> pos_relatii_in_out,
                                int nr_relatii_input, int nr_relatii_output){
        Serviciu s;
        //nume
        s.nume = u.genereaza_nume(nume_folosite,"Serv_");
        //parametrii
        vector<string> nume_parametrii;
        for (int i=1;i<=nr_param_input+nr_param_output;i++){
            Parametru param;
            param.nume = u.genereaza_nume(nume_parametrii,string("Nume_"));
            if (i<=nr_param_input){
                param.tip = pos_param_input[rand()% (pos_param_input.size()-1)];
                s.input_param.push_back(param);
            }
            else{
                param.tip = pos_param_output[rand()% (pos_param_output.size()-1)];
                s.output_param.push_back(param);
            }
        }
        //relatii
        for (int i=1;i<=nr_relatii_input;i++){
            Parametru a = s.input_param[rand()%(s.input_param.size()-1)];
            Parametru b = s.input_param[rand()%(s.input_param.size()-1)];
            if (a==b)
                continue;
            pair <Concept, Concept> p = make_pair(a.tip,b.tip);
            if (pos_relatii_input.find(p)!=pos_relatii_input.end()){
                Relatie r;
                Relatie r_posibila=pos_relatii_input[p][rand()%(pos_relatii_input[p].size())];
                r.Ta=a.tip; r.Tb=b.tip;
                r.a=a.nume;  r.b=b.nume;
                r.nume=r_posibila.nume;//"Rel_"+genereaza_string_random(lungime_nume);
                if (find(s.relatii.begin(),s.relatii.end(),r)==s.relatii.end())
                    s.relatii.push_back(r);

            }
        }

        for (int i=1;i<=nr_relatii_output;i++){
            Parametru a, b;

            if (rand()%2){
                a = s.input_param[rand()%(s.input_param.size()-1)];
                b = s.input_param[rand()%(s.input_param.size()-1)];
            }else{
                a = s.input_param[rand()%(s.input_param.size()-1)];
                b = s.output_param[rand()%(s.output_param.size()-1)];
            }
            if (a==b)
                continue;
            pair<Concept, Concept> p = make_pair(a.tip,b.tip);
            if (pos_relatii_in_out.find(p)!=pos_relatii_in_out.end()){
                Relatie r;
                Relatie r_posibila =pos_relatii_in_out[p][rand()%(pos_relatii_in_out[p].size())];
                r.Ta=a.tip; r.Tb=b.tip;
                r.a=a.nume;  r.b=b.nume;
                r.nume=r_posibila.nume;//"Rel_"+genereaza_string_random(lungime_nume);
                if (find(s.relatii.begin(),s.relatii.end(),r)==s.relatii.end())
                    s.relatii.push_back(r);
            }
        }
        return s;
    }
};

struct Relatii_Manager{
    vector <Relatie> toate_relatiile;
    vector <string> nume_folosite;
    U u;

    vector <Relatie> genereaza_relatii_noi(vector <Concept> concepte_vechi, vector<Concept> concepte_noi, int nr_relatii){
        vector <Relatie> relatii;
        for (int i=1;i<=nr_relatii;i++){
            Relatie r;
            r.nume = u.genereaza_nume(nume_folosite,string("Rel_"));// "Rel_"+genereaza_string_random(lg_nume);
            r.a = u.genereaza_nume(nume_folosite,string("N1_"));//genereaza_string_random(lg_nume);
            r.b = u.genereaza_nume(nume_folosite,string("N2_"));//genereaza_string_random(lg_nume);
            if (rand()%2){
                r.Ta = concepte_vechi[rand()% (concepte_vechi.size()-1)];
                r.Tb = concepte_noi[rand()% (concepte_noi.size()-1)];
            }else{
                r.Ta = concepte_noi[rand()% (concepte_noi.size()-1)];
                r.Tb = concepte_noi[rand()% (concepte_noi.size()-1)];
            }
            relatii.push_back(r);
        }
        return relatii;
    }
    vector <Relatie> genereaza_relatii_noi(vector<Concept> concepte, int nr_relatii){
        vector <Relatie> relatii;
        for (int i=1;i<=nr_relatii;i++){
            Relatie r;
            r.nume = u.genereaza_nume(nume_folosite,string("Rel_"));
            r.a = u.genereaza_nume(nume_folosite,string("N1_"));//genereaza_string_random(lg_nume);
            r.b = u.genereaza_nume(nume_folosite,string("N2_"));//genereaza_string_random(lg_nume);
            r.Ta = concepte[rand()% (concepte.size()-1)];
            r.Tb = concepte[rand()% (concepte.size()-1)];
            relatii.push_back(r);
        }
        return relatii;
    }
};

#include<stdio.h>

struct Cst{
   /*int nr_nivele=5, nr_concepte = 4,
        lungime_nume=3, lungime_nume_param=3, lungime_nume_serv = 3, lg_nume = 3,
        nr_serv_pe_nivel = 5,
        nr_param_input = 4, nr_param_output=3,
        nr_relatii = 6, nr_relatii_noi = 6, nr_relatii_noi_input = 6,
        nr_relatii_inp_serv = 7, nr_relatii_out_serv = 7,
        nr_relatii_irelevante=0, nr_servicii_irelevante=0,
        nr_reguli = 60 ,
        query_nr_rel_inp = 25, query_nr_rel_out = 10, query_nr_param_max_inp=8, query_nr_param_max_out=5;
*/
    int nr_nivele, nr_concepte,
        lungime_nume, lungime_nume_param, lungime_nume_serv, lg_nume,
        nr_serv_pe_nivel,
        nr_param_input, nr_param_output,
        nr_relatii, nr_relatii_noi, nr_relatii_noi_input,
        nr_relatii_inp_serv, nr_relatii_out_serv,
        nr_relatii_irelevante, nr_servicii_irelevante,
        nr_reguli,
        query_nr_rel_inp, query_nr_rel_out, query_nr_param_max_inp, query_nr_param_max_out;

    void citeste(){
        freopen("constante.txt","r",stdin);
        scanf("%d %d", &nr_nivele, &nr_concepte);
        scanf("%d %d %d %d", &lungime_nume, &lungime_nume_param, &lungime_nume_serv, &lg_nume);
        scanf("%d",&nr_serv_pe_nivel);
        scanf("%d %d",&nr_param_input, &nr_param_output);
        scanf("%d %d %d",&nr_relatii, &nr_relatii_noi, &nr_relatii_noi_input);
        scanf("%d %d", &nr_relatii_inp_serv, &nr_relatii_out_serv);
        scanf("%d %d", &nr_relatii_irelevante, &nr_servicii_irelevante);
        scanf("%d",&nr_reguli);
        scanf("%d %d %d %d", &query_nr_rel_inp, &query_nr_rel_out, &query_nr_param_max_inp, &query_nr_param_max_out);
        /*nr_serv_pe_nivel = 3;
        nr_nivele = 6;
        nr_reguli = 10;
        nr_relatii_inp_serv = 4;
        nr_relatii_out_serv = 7;
        //query_nr_rel_inp = 5;
        //query_nr_rel_out = 3;
        nr_param_input = 5;
        nr_param_output = 8;
        nr_servicii_irelevante = 100;
        nr_relatii_irelevante=100;

        query_nr_rel_inp = 20;
        query_nr_rel_out = 5;
        query_nr_param_max_inp = 10;
        query_nr_param_max_out = 4;

        nr_nivele = 6;
        nr_serv_pe_nivel = 6;
        nr_concepte = 3;

        nr_param_input = 5;
        nr_param_output = 8;
        nr_relatii_inp_serv = 4;
        nr_relatii_out_serv = 7;


lungime_nume=7;
lungime_nume_param=7;
lungime_nume_serv = 7;
lg_nume = 7;

nr_relatii = 6;
nr_relatii_noi = 6;
nr_relatii_noi_input = 6;
*/

    }
};

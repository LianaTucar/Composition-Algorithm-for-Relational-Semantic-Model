import subprocess
import time
import os
import random

import shutil

dir_teste = "teste noi 3/"

nr_nivele = ""
nr_concepte = ""
lungime_nume=7
lungime_nume_param=7
lungime_nume_serv = 7
lg_nume = 7
nr_serv_pe_nivel = ""
nr_param_input = ""
nr_param_output = ""
nr_relatii = 4
nr_relatii_noi = 4
nr_relatii_noi_input = 4
nr_relatii_inp_serv = ""
nr_relatii_out_serv = ""
nr_relatii_irelevante = ""
nr_servicii_irelevante = ""
nr_reguli = ""
query_nr_rel_inp = ""
query_nr_rel_out = ""
query_nr_param_max_inp = ""
query_nr_param_max_out = ""

def afiseaza():
    f = open("constante.txt", "w")
    f.write(str(nr_nivele) + " " + str(nr_concepte) + " " + str(lungime_nume) + " " + str(lungime_nume_param) + " " +
            str(lungime_nume_serv) + " " +
            str(lg_nume) + " " +
            str(nr_serv_pe_nivel) + " " +
            str(nr_param_input) + " " +
            str(nr_param_output) + " " +
            str(nr_relatii) + " " +
            str(nr_relatii_noi) + " " +
            str(nr_relatii_noi_input) + " " +
            str(nr_relatii_inp_serv) + " " +
            str(nr_relatii_out_serv) + " " +
            str(nr_relatii_irelevante) + " " +
            str(nr_servicii_irelevante) + " " +
            str(nr_reguli) + " " +
            str(query_nr_rel_inp) + " " +
            str(query_nr_rel_out) + " " +
            str(query_nr_param_max_inp) + " " +
            str(query_nr_param_max_out))
    f.close()

def schimba_val():
    global nr_nivele, nr_concepte,lungime_nume,lungime_nume_param, lungime_nume_serv,lg_nume,nr_serv_pe_nivel
    global nr_param_input,nr_param_output,nr_relatii,nr_relatii_noi,nr_relatii_noi_input,nr_relatii_inp_serv,nr_relatii_out_serv,nr_relatii_irelevante,nr_servicii_irelevante,nr_reguli
    global query_nr_rel_inp,query_nr_rel_out ,query_nr_param_max_inp,query_nr_param_max_out


    # nr_serv_pe_nivel = 3
    # nr_nivele = 6
    nr_reguli = random.randint(1,10)
    nr_relatii_inp_serv = 4
    nr_relatii_out_serv = 7
    nr_param_input = random.randint(1,5)
    nr_param_output = random.randint(1,8)
    nr_servicii_irelevante = random.randint(1,50) #100
    nr_relatii_irelevante = random.randint(1,30) #100

    query_nr_rel_inp = random.randint(1,20) #20
    query_nr_rel_out = random.randint(1,5) #5
    query_nr_param_max_inp = random.randint(1,10) #10
    query_nr_param_max_out = random.randint(1,4) #4

    nr_nivele = random.randint(1,6) #6
    nr_serv_pe_nivel = random.randint(1,6) #6
    nr_concepte = random.randint(1,4) #3

    nr_param_input = random.randint(1,5)#5
    nr_param_output = random.randint(1,8) #8
    nr_relatii_inp_serv = random.randint(1,4) #4
    nr_relatii_out_serv = random.randint(1,7) #7

    lungime_nume = 7
    lungime_nume_param = 7
    lungime_nume_serv = 7
    lg_nume = 7

    nr_relatii = random.randint(1,6)
    nr_relatii_noi = random.randint(1,6)
    nr_relatii_noi_input = random.randint(1,6)



    '''  nr_nivele = random.randint(2, 6)
    nr_serv_pe_nivel = random.randint(2, 6)
    nr_concepte = random.randint(2, 6)

    nr_param_input = random.randint(2, 5)
    nr_param_output = random.randint(2, 8)
    nr_relatii_inp_serv = random.randint(0, 4)
    nr_relatii_out_serv = random.randint(0, 7)


    nr_relatii = nr_relatii_noi = nr_relatii_noi_input = random.randint(2, 4)

    nr_reguli = random.randint(0, 10)
    nr_servicii_irelevante=random.randint(0,30)
    nr_relatii_irelevante=random.randint(0, 10)

    query_nr_rel_inp = random.randint(0, 20)
    query_nr_rel_out = random.randint(0, 5)
    query_nr_param_max_inp = random.randint(2, 10)
    query_nr_param_max_out = random.randint(1, 4)
'''
    '''
    nr_serv_pe_nivel = 6
    nr_nivele = 6
    nr_reguli = 10
    nr_relatii_inp_serv = 4
    nr_relatii_out_serv = 7
    query_nr_rel_inp = 5
    query_nr_rel_out = 3
    nr_param_input = 5
    nr_param_output = 8
    nr_servicii_irelevante = 100
    nr_relatii_irelevante = 100'''

def citeste_rezultate():
    g = open("stats.txt", "r")
    rez = g.readline()
    nr_serv = int(g.readline())
    nr_reg = int(g.readline())
    return (rez, nr_serv, nr_reg)


def ruleaza_algoritm(jarFile):
    rez_alg = "Not Solved" #oare e bine asa???mhm...
    nr_serv_alg = nr_reg_alg = 0
    t = 0
    time.sleep(3)
    tp_inceput = time.time()
    try:
        proc = subprocess.call(["java", "-jar", jarFile], timeout=140, shell=False)
        # proc.teminate()
        # proc.wait()
    except:
        print("trist NOU")
        t = 1
    tp_final = time.time()
    dif_tp = tp_final - tp_inceput
    if t == 0:
        try:
            (rez_alg, nr_serv_alg, nr_reg_alg) = citeste_rezultate()
            print("GATA")
        except:
            print("das")
    time.sleep(3)
    return (dif_tp, rez_alg, nr_serv_alg, nr_reg_alg)

def pastreaza_test(nr_serv, nr_reg, dif_tp):
    nr_serv_total = nr_servicii_irelevante + nr_serv_pe_nivel * (nr_nivele-1) #nr_nivele
    new_name = str(dif_tp)+" "+str(nr_reg) + " " + str(nr_serv) + " " + str(nr_serv_total) + " " + str(time.time())

    os.mkdir(dir_teste + new_name)

    try:
        for s in ["ontology.txt","query.txt","repository.txt", "constante.txt"]:
            os.rename(s, dir_teste + new_name+"/"+s)
    except Exception as e:
        print("in pastreaza "+ str(e))
        os.rmdir(dir_teste + new_name)
    #os.rename("ontology.txt", "teste/ontology.txt")
    #shutil.move("ontology.txt", "path/to/new/destination/for/file.foo")

for i in range(300):
    schimba_val()
    afiseaza()
    print("param inp" + str(nr_param_input))
    print("param out" + str(nr_param_output))

    try:
        print("call suprocess")
        procc= subprocess.call(["generator.exe"], timeout=40, shell=False)
        #procc.teminate()
        #procc.wait()
        #os.startfile("generator.exe")
    except Exception as e:
        print("in catch "+str(e))
        continue

    print(i)
    print("Inainte de ruleaza Paul")
    dif_tp_Paul, rez_alg_Paul, nr_serv_alg_Paul, nr_reg_alg_Paul = ruleaza_algoritm("Alg Paul.jar")
    print("Gata alg Paul. Incepe alg Liana")
    dif_tp_Liana, rez_alg_Liana, nr_serv_alg_Liana, nr_reg_alg_Liana = ruleaza_algoritm("algoritmLianaCuReguli.jar")
    print("Gata alg Liana")

    print("rez alg L"+rez_alg_Liana)
    print("rez alg P" + rez_alg_Liana)
    if (rez_alg_Liana != "Not Solved") or (rez_alg_Paul != "Not Solved") and ((dif_tp_Liana>=0.25) or (dif_tp_Paul>=0.25)):
        print("pastreaza")
        pastreaza_test(nr_serv_alg_Liana, nr_reg_alg_Liana, dif_tp_Liana)
    else:
        print("nu pastreaza")
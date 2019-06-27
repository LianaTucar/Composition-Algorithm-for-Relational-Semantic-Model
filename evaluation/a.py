import os, time
import shutil, subprocess
from xlwt import Workbook

dir_teste = "../generator teste/main - chestie_automata/adunate pt scris"
dir_pt_testat = "dir pt testat/"

algoritmi = ["algoritmLiana2.jar", "algoritmLianaCuReguli.jar", "AlgPaulCorectat.jar"]

def citeste_rezultate():
    g = open("stats.txt", "r")
    rez = g.readline()
    nr_serv = int(g.readline().split()[-1])
    nr_reg = int(g.readline().split()[-1])
    exec_time = float(g.readline().split()[-1])
    return (rez, nr_serv, nr_reg, exec_time)


def run_algorithm(jarFile):
    os.chdir(dir_pt_testat)

    rez_alg = "Not Solved"
    nr_serv_alg = nr_reg_alg = exec_time = 0
    t = 1

    if os.path.exists("stats.txt"):
        os.remove("stats.txt")

    try:
        proc = subprocess.call(["java", "-jar", jarFile], timeout=120, shell=False)
        t = 0
        # proc.teminate()
        # proc.wait()
    except:
        print("trist NOU")
        t = 1

    if t == 0:
        try:
            (rez_alg, nr_serv_alg, nr_reg_alg, exec_time) = citeste_rezultate()
            print("GATA")
        except:
            print("das")
    os.chdir("../")
    return (rez_alg, nr_serv_alg, nr_reg_alg, exec_time)

def write_result1(lin, col_inceput, rez, nr_serv, nr_reg, exec_time):
    sheet1.write(lin, col_inceput, rez)
    sheet1.write(lin, col_inceput+1, nr_serv)
    sheet1.write(lin, col_inceput+2, nr_reg)
    sheet1.write(lin, col_inceput+3, exec_time)

def write_result(test_name,
                rez_alg_paul, nr_serv_alg_paul, nr_reg_alg_paul, exec_time_paul,
                rez_alg_liana, nr_serv_alg_liana, nr_reg_alg_liana, exec_time_liana):

    global nr_test, sheet1
    nr_test += 1
    sheet1.write(nr_test, 0, test_name)

    sheet1.write(nr_test, 1, rez_alg_paul)
    sheet1.write(nr_test, 2, nr_serv_alg_paul)
    sheet1.write(nr_test, 3, nr_reg_alg_paul)
    sheet1.write(nr_test, 4, exec_time_paul)

    sheet1.write(nr_test, 5, rez_alg_liana)
    sheet1.write(nr_test, 6, nr_serv_alg_liana)
    sheet1.write(nr_test, 7, nr_reg_alg_liana)
    sheet1.write(nr_test, 8, exec_time_liana)

nr_test = 5
wb = Workbook()
sheet1 = wb.add_sheet('Sheet 1')

for test in os.scandir(dir_teste):
    nr_test +=1
    print("test________________________________________________________________ "+str(nr_test))
    test_name = os.path.basename(test)
    for file in os.scandir(test):
        base_name = os.path.basename((file.path))
        newPath = shutil.copy(file.path, dir_pt_testat+base_name)

    sheet1.write(nr_test, 0, test_name)

    col_inceput = 1
    for algoritm in algoritmi:
        rez_alg, nr_serv_alg, nr_reg_alg, exec_time = run_algorithm(algoritm)
        write_result1(nr_test, col_inceput, rez_alg, nr_serv_alg, nr_reg_alg, exec_time)
        col_inceput += 4

    print("end test")
wb.save('evaluare teste adunate.xls')

import subprocess
import time
import os

f = open('timpi.txt', 'w')
os.chdir('./01')


for t in range(1, 9):
    os.chdir('../0'+str(t))
    tp_inceput = time.time()
    proc = subprocess.call(['java', '-jar', 'RelationalWSC_nou.jar'], timeout = 40, shell=False)
    tp_final = time.time()
    f.write(str(t)+' '+str(tp_final - tp_inceput)+'\n')
    time.sleep(3)
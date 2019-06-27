#include <string>
#include<vector>
#include<algorithm>
#include"constante.cpp"
using namespace std;

static const char alphanum[] = "0123456789";
       // "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        /*"abcdefghijklmnopqrstuvwxyz";
*/
struct U{
    Cst cst;
    string genereaza_string_random(int lg){
        string s="";
        for (int i = 0; i < lg; ++i) {
            s+=alphanum[rand() % (sizeof(alphanum) - 1)];
        }
        return s;
    }

    string genereaza_nume(vector<string> &nume_folosite, string prefix){
        cst.citeste();
        string nume="";
        while (1){
            nume = prefix+genereaza_string_random(cst.lg_nume);
            if (find(nume_folosite.begin(),nume_folosite.end(),nume)==nume_folosite.end())
                break;
            }
        nume_folosite.push_back(nume);
        return nume;
    }
};

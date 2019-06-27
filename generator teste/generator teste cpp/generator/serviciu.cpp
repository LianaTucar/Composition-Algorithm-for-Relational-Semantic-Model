#include <string>
#include <vector>
#include<unordered_map>
#include <algorithm>
#include<set>
using namespace std;

struct Concept{
    string nume;
    bool noise;
    // concept parinte;

    bool operator==(const Concept& right)const{
        return nume == right.nume;
    }
    size_t operator<(const Concept& right)const{
        return nume<right.nume;
    }
};

struct Parametru{
    string nume;
    Concept tip;

    string toString(){
        return "("+nume+", "+tip.nume+")";
    }

    string toString2(){
        return tip.nume+" "+nume;
    }

    bool operator==(const Parametru& right)const{
        return tip==right.tip;
    }
};

struct Relatie{
    string nume;
    Concept Ta, Tb;
    string a, b;

    string toString(){
        return nume+"( "+a+", "+b+")";
    }

    string toString2(){
        return nume+" "+a+" "+b;
    }

    bool operator==(const Relatie& right)const{
        return a==right.a && b==right.b;
    }
    bool operator<(const Relatie& right)const{
        if (a!=right.a)
            return b<right.b;
        return (a<right.a);
    }

};

struct Serviciu{
    vector <Parametru> input_param;
    vector <Parametru> output_param;
    string nume;
    vector <Relatie> relatii;

    string extrage_nume_param(vector<Parametru> v1, vector<Parametru>v2, Concept c){
        Parametru p;
        p.tip=c;
        auto f1=find(v1.begin(),v1.end(),p);
        if (f1!=v1.end())
            return (*f1).nume;
        auto f2=find(v2.begin(),v2.end(),p);
        if (f2!=v2.end())
            return (*f2).nume;
    }

    string toString(){
        string str = "nume:"+nume+"\n";
        str+="input: ";
        for (auto it = input_param.begin();it!=input_param.end();it++)
            str+=(*it).toString()+" ";
        str+="\n";
        str+="output: ";
        for (auto it = output_param.begin();it!=output_param.end();it++)
            str+=(*it).toString()+" ";
        str+="\nrelatii: ";
        eliminare_duplicate_relatii();
        for (auto it = relatii.begin();it!=relatii.end();it++){
            Relatie rel = *it;
            string numeX=extrage_nume_param(input_param, output_param, rel.Ta);
            string numeY=extrage_nume_param(input_param, output_param, rel.Tb);
            str+=(*it).toString()+", ";
        }

        return str;
    }

     string toString2(){
        string str =nume+"\n";
        for (auto it = input_param.begin();it!=input_param.end();it++)
            str+=(*it).toString2()+" ";
        str+="\n";
        for (auto it = output_param.begin();it!=output_param.end();it++)
            str+=(*it).toString2()+" ";
        str+="\n";
        eliminare_duplicate_relatii();
        for (auto it = relatii.begin();it!=relatii.end();it++){
            Relatie rel = *it;
            string numeX=extrage_nume_param(input_param, output_param, rel.Ta);
            string numeY=extrage_nume_param(input_param, output_param, rel.Tb);
            str+=(*it).toString2()+" ";
        }

        return str;
    }

    void eliminare_duplicate_relatii(){
        set<Relatie> s( relatii.begin(), relatii.end() );
        relatii.assign( s.begin(), s.end() );
    }

    bool operator==(const Serviciu& right)const{
        return nume == right.nume;
    }
    //adauga si relatii acusi
};

struct Regula{
    string nume;
    Relatie r1, r2, r3;
};

struct Regula2{
    string nume;
    Relatie r1, r2;
};

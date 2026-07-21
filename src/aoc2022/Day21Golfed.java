package aoc2022;
class U{
String[]m=new String[1<<22];
long N=1L<<63;
boolean P;
int h(String s,int i){return s.substring(i,i+4).hashCode();}
String s(boolean p,String[]I){
for(var l:I)m[h(l,0)]=l.substring(6);
if(P=p)return""+q(3506402,N);
var r=m[3506402];int a=h(r,0),b=h(r,7);
return""+(q(a,N)==N?q(a,q(b,N)):q(b,q(a,N)));
}
long q(int k,long t){
if(!P&k==3214190)return t;
var x=m[k];
if(x.charAt(0)<58)return (new Long(x));
int l=h(x,0),r=h(x,7);long a=q(l,N),b=q(r,N);var c=a==N?b:a;
var o=x.charAt(5);
return t==N?a==N|b==N?N:o<43?a*b:o<44?a+b:o<46?a-b:a/b:q(a==N?l:r,o<43?t/c:o<44?t-c:o<46?a==N?t+b:a-t:a==N?t*b:a/t);
}
}

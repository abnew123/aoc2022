package aoc2022;
class U{
String[]m=new String[1<<22];
long N=1L<<63;
boolean P;
int h(String s,int i){return s.substring(i,i+4).hashCode();}
String s(boolean p,String[]I){
for(var l:I)m[h(l,0)]=l.substring(6);
if(P=p)return""+q(3506402);
var r=m[3506402];int a=h(r,0),b=h(r,7);
return""+(q(a)==N?n(a,q(b)):n(b,q(a)));
}
long q(int k){
if(!P&k==3214190)return N;
var x=m[k];
if(x.charAt(0)<58)return $.l(x);
long a=q(h(x,0)),b=q(h(x,7));
var o=x.charAt(5);return a==N|b==N?N:o==43?a+b:o==45?a-b:o==42?a*b:a/b;
}
long n(int k,long t){
if(k==3214190)return t;
var x=m[k];int l=h(x,0),r=h(x,7);
long a=q(l),b=q(r);
var o=x.charAt(5);
return a==N?n(l,o==43?t-b:o==45?t+b:o==42?t/b:t*b):n(r,o==43?t-a:o==45?a-t:o==42?t/a:a/t);
}
}

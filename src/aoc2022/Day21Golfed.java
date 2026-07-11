package aoc2022;
import java.util.*;
class U{
Map<String,String>m;
long N=1L<<63;
boolean P;
String s(boolean p,String[]I){
m=$.h();
for(var l:I)m.put(l.substring(0,4),l.substring(6));
if(P=p)return""+q("root");
String r=m.get("root"),a=r.substring(0,4),b=r.substring(7);
var x=q(a);
return""+(x==N?n(a,q(b)):n(b,x));
}
long q(String k){
if(!P&k.equals("humn"))return N;
var x=m.get(k);
if(x.charAt(0)<58)return $.l(x);
long a=q(x.substring(0,4)),b=q(x.substring(7));
return a==N|b==N?N:c(x.charAt(5),a,b);
}
long n(String k,long t){
if(k.equals("humn"))return t;
String x=m.get(k),l=x.substring(0,4),r=x.substring(7);
long a=q(l),b=q(r);
var o=x.charAt(5);
return a==N?n(l,o==43?t-b:o==45?t+b:o==42?t/b:t*b):n(r,o==43?t-a:o==45?a-t:o==42?t/a:a/t);
}
long c(char o,long a,long b){return o==43?a+b:o==45?a-b:o==42?a*b:a/b;}
}

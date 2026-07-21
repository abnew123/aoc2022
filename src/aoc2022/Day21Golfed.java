package aoc2022;
class U{
String[]m=new String[1<<22];
long N=1L<<63;
int h(String s,int i){return s.substring(i,i+4).hashCode();}
String s(boolean p,String[]I){
for(var l:I)m[h(l,0)]=l.substring(6);
if(!p){m[3506402]=m[3506402].replaceAll("\\W","-");m[3214190]="";}
return""+q(3506402,p?N:0);
}
long q(int k,long t){
var x=m[k];
if(x=="")return t;
if(x.charAt(0)<58)return new Long(x);
int l=h(x,0),r=h(x,7);long a=q(l,N),b=q(r,N),c=a^b^N;
var o=x.charAt(5)&7;
return t==N?a>N&b>N?o<3?a*b:o<4?a+b:o<6?a-b:a/b:N:q(a<b?l:r,o<3?t/c:o<4?t-c:o<6?a<b?t+c:c-t:a<b?t*c:c/t);
}
}

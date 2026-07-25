package aoc2022;
class V{
int x,y=50,d,n;
String s(boolean p,String[]I){
for(int C:(I[201]+"<").getBytes())if(C<58)n=n*10+C-48;else{
for(;n>0;n--){
int c=d,u=x,w=y;
for(;(4556>>(w=w+(1-d)%2&255)/50*6+(u=u+(2-d)%2&255)/50&1)<1;)if(!p){
int k=(d%2>0?y:x)%50,z="W1k@P'HP8PPPy,fEPPM33".charAt(x/50*8+y/50*4+d-6);c=z/10%4;
k=z<52&d<2?49:z>80?49-k:k;
int e=c/2*49,X=c%2>0?e:k;u=z%10/3*50+X;w=z%10%3*50+e+k-X;
break;
}
if(I[u].charAt(w)>35){x=u;y=w;d=c;}
}
d=d-C/3&3;
}
return 1000*++x+4*++y+d+"";
}
}

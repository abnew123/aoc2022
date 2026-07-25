package aoc2022;
class V{
String[]g;int R,x,y,d,n;
String s(boolean p,String[]I){
g=I;R=I.length-1;
y=50;
for(int C:(I[R]+"<").getBytes())if(C<58)n=n*10+C-48;else{
for(;n>0;n--){
int c=d,u=x,w=y;
for(;G(u=u+(2-d)%2&255,w=w+(1-d)%2&255)<33;)if(!p){
int k=(d%2>0?y:x)%50,z="PPPPPPW1k@P'PPPPHP8PPPPPPPy,fEPPPPPPM33".charAt(x/50*12+y/50*4+d);c=z/10%4;
k=z<52&d<2?49:z>80?49-k:k;
int e=c/2*49,X=c%2>0?e:k;u=z%10/3*50+X;w=z%10%3*50+e+k-X;
break;
}
if(G(u,w)>35){x=u;y=w;d=c;}
}
d=d-C/3&3;
}
return 1000*++x+4*++y+d+"";
}
int G(int x,int y){
return x<R&&y<g[x].length()?g[x].charAt(y):0;
}
}

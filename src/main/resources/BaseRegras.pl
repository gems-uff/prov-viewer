provenancedata(IDPARENT, IDPROVENANCEDATA, IDVERTEX, IDEDGE) :- vertex(IDPROVENANCEDATA, IDVERTEX), edge(IDPROVENANCEDATA, IDEDGE).
provenancedata(IDPARENT, IDPROVENANCEDATA, IDEDGE) :- edge(IDPROVENANCEDATA, IDEDGE).
provenancedata(IDPARENT, IDPROVENANCEDATA, IDVERTEX) :- vertex(IDPROVENANCEDATA, IDVERTEX).
edge(IDPARENT, IDEDGE, ID, TYPE, LABEL, VALUE, SOURCEID, TARGETID) :- id(IDEDGE, IDID,ID), type(IDEDGE, IDTYPE,TYPE), label(IDEDGE, IDLABEL,LABEL), value(IDEDGE, IDVALUE,VALUE), sourceid(IDEDGE, IDSOURCEID,SOURCEID), targetid(IDEDGE, IDTARGETID,TARGETID).
vertex(IDPARENT, IDVERTEX, ID, TYPE, LABEL, DATE, IDATTRIBUTE, DETAILS) :- id(IDVERTEX, IDID,ID), type(IDVERTEX, IDTYPE,TYPE), label(IDVERTEX, IDLABEL,LABEL), date(IDVERTEX, IDDATE,DATE), attribute(IDVERTEX, IDATTRIBUTE), details(IDVERTEX, IDDETAILS,DETAILS).
vertex(IDPARENT, IDVERTEX, ID, TYPE, LABEL, DATE, DETAILS) :- id(IDVERTEX, IDID,ID), type(IDVERTEX, IDTYPE,TYPE), label(IDVERTEX, IDLABEL,LABEL), date(IDVERTEX, IDDATE,DATE), details(IDVERTEX, IDDETAILS,DETAILS).
attribute(IDPARENT, IDATTRIBUTE, NAME, VALUE) :- name(IDATTRIBUTE, IDNAME,NAME), value(IDATTRIBUTE, IDVALUE,VALUE).

%NewRules
vertices(L) :- findall(X, vertex(_, _, X, _, _, _, _, _),R), sort(R,L).
add(X, L, [X|L]).
threshold(X,Y,VAL) :- abs(X - Y) < VAL.

%StandardDeviation
stddev(List,Stddev) :-
	length(List,N),
	sum(List,Sum),
	squares(List, SqList),
	sum(SqList,SumSquares),
	variance(N,Sum,SumSquares,Variance),
	sqrt(Variance,Stddev).

variance(N,__,0) :- N=< 1,!.
variance(N,Sum,SumSq,Variance) :- Variance is (SumSq - (Sum*Sum/N))/(N-1).

sum([Item], Item).
sum([Item1,Item2 | Tail], Total) :- sum([Item1+Item2|Tail], Total).

squares([X|Xs],[Y|Ys]) :- Y is X*X, squares(Xs,Ys).
squares([],[]).

%Misc
max(X,Y, Z):- X >= Y -> Z is X; X < Y -> Z is Y.
min(X,Y, Z):- X =< Y -> Z is X; X > Y -> Z is Y.
first([First|Rest], First).	

%AttributeSTDDEV
attribute_value(X, ATT) :- attribute(_, _, ATT, Y), atom_number(Y, X).
attributes(L, ATT) :- findall(X, attribute_value(X, ATT),L).
attstddev(V,L, ATT) :- 
	attributes(L, ATT),
	stddev(L,V).
	
%Paths
path(X, Y) :- path(X,Y,[]); path(Y,X,[]).
path(X,Y,_) :- edge(_, _, _, _, _, _, X, Y); edge(_, _, _, _, _, _, Y, X).
path(X,Y,V) :- \+ member(X, V), edge(_, _, _, _, _, _, X, Z), path(Z,Y, [X|V]).

pathType(X, Y, TYPE) :- pathType(X,Y,[],TYPE); pathType(Y,X,[],TYPE).
pathType(X,Y,_,TYPE) :- edge(_, _, _, TYPE, _, _, X, Y); edge(_, _, _, TYPE, _, _, Y, X).
pathType(X,Y,V,TYPE) :- \+ member(X, V), edge(_, _, _, TYPE, _, _, X, Z), pathType(Z,Y, [X|V],TYPE).

pathAgent(X, Y) :- pathAgent(X,Y,[]); pathAgent(Y,X,[]).
pathAgent(X,Y,_) :- edge(_, _, _, _, _, _, X, Y), vertex(_, _, Y, 'Agent', _, _, _).
pathAgent(X,Y,_) :- edge(_, _, _, _, _, _, Y, X), vertex(_, _, X, 'Agent', _, _, _).
pathAgent(X,Y,V) :- \+ member(X, V), edge(_, _, _, _, _, _, X, Z), pathAgent(Z,Y, [X|V]).

%DummyFilter
filterlower(X, Y, Z) :- vertex(_, _, X, _, _, _, IDATTRIBUTE, _), attribute(_, IDATTRIBUTE, Y, VALUE), atom_number(VALUE, W), W < Z.
filtergreater(X, Y, Z) :- vertex(_, _, X, _, _, _, IDATTRIBUTE, _), attribute(_, IDATTRIBUTE, Y, VALUE), atom_number(VALUE, W), W > Z.

%CollapseFilter
filter(V1, V2, ATT,TYPE) :- 
	pathType(V1,V2,TYPE), 
	vertex(_, _, V1, _, _, _, IDATTRIBUTE1, _), 
	vertex(_, _, V2, _, _, _, IDATTRIBUTE2, _), 
	attribute(_, IDATTRIBUTE1, ATT, VALUE1), 
	attribute(_, IDATTRIBUTE2, ATT, VALUE2), 
	atom_number(VALUE1, W1), 
	atom_number(VALUE2, W2), 
	attstddev(VAL, L, ATT),
	threshold(W1,W2,VAL), 
	not(IDATTRIBUTE1 = IDATTRIBUTE2).

collapsevertex(X, ATT,TYPE) :- 
	setof(X,filter(X,Y,ATT,TYPE),L), 
	add(Y,L,L2),
	sort(L2,X).

collapse(L,ATT,TYPE) :- setof(X,collapsevertex(X,ATT,TYPE),L).

%setof(X,collapsevertex(X,'Hours','Neutral'),L).
%set_prolog_flag(toplevel_print_options, [quoted(true), portray(true), max_depth(100), priority(699)]).

filter2(Lant,ATT,TYPE,MIN,MAX,[X|Lresp]) :- 
	first(Lant,Y),
	pathType3(Y,X,TYPE), 
	vertex(_, _, Y, _, _, _, IDATTRIBUTE1, _), 
	vertex(_, _, X, _, _, _, IDATTRIBUTE2, _), 
	attribute(_, IDATTRIBUTE1, ATT, VALUE1), 
	attribute(_, IDATTRIBUTE2, ATT, VALUE2),
	atom_number(VALUE1, W1),
	atom_number(VALUE2, W2),
	max(W1,MAX,MX1),
	min(W1,MIN,MN1),
	max(W2,MX1,MX2),
	min(W2,MN1,MN2),
	attstddev(STD, L, ATT),
	threshold(MX2,MN2,STD),
	not(IDATTRIBUTE1 = IDATTRIBUTE2),
	filter3([X|Lant],ATT,TYPE,MN2,MX2,Lresp).

filter3(_,ATT,TYPE,MIN,MAX,[]).	
filter3(Lant,ATT,TYPE,MIN,MAX,[X|Lresp]) :- 
	first(Lant,Y),
	pathType3(Y,X,TYPE), 
	vertex(_, _, X, _, _, _, IDATTRIBUTE2, _), 
	attribute(_, IDATTRIBUTE2, ATT, VALUE2),
	atom_number(VALUE2, W2),
	max(W2,MAX,MX2),
	min(W2,MIN,MN2),
	attstddev(STD, L, ATT),
	threshold(MX2,MN2,STD),
	filter3([X|Lant],ATT,TYPE,MN2,MX2,Lresp).
	
	
pathType3(X, Y, TYPE) :- edge(_, _, _, TYPE, _, _, Y, X), vertex(_, _, X, T, _, _, _), vertex(_, _, Y, T, _, _, _).
	
c(R, ATT, TYPE) :- 
	MIN is 999999 ,
	MAX is -999999 ,
	filter2([X],ATT,TYPE,MIN,MAX,L),
	add(X,L,R).

	
c2(L,ATT,TYPE) :- setof(X,c(X,ATT,TYPE),L).

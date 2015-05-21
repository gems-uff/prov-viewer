provenancedata(IDPARENT, IDPROVENANCEDATA, IDVERTICES, IDEDGES) :- vertices(IDPROVENANCEDATA, IDVERTICES), edges(IDPROVENANCEDATA, IDEDGES).
vertices(IDPARENT, IDVERTICES, IDVERTEX) :- vertex(IDVERTICES, IDVERTEX).
edges(IDPARENT, IDEDGES, IDEDGE) :- edge(IDEDGES, IDEDGE).
edge(IDPARENT, IDEDGE, ID, TYPE, LABEL, VALUE, SOURCEID, TARGETID) :- id(IDEDGE, IDID,ID), type(IDEDGE, IDTYPE,TYPE), label(IDEDGE, IDLABEL,LABEL), value(IDEDGE, IDVALUE,VALUE), sourceid(IDEDGE, IDSOURCEID,SOURCEID), targetid(IDEDGE, IDTARGETID,TARGETID).
vertex(IDPARENT, IDVERTEX, ID, TYPE, LABEL, DATE, IDATTRIBUTES) :- id(IDVERTEX, IDID,ID), type(IDVERTEX, IDTYPE,TYPE), label(IDVERTEX, IDLABEL,LABEL), date(IDVERTEX, IDDATE,DATE), attributes(IDVERTEX, IDATTRIBUTES).
attributes(IDPARENT, IDATTRIBUTES, IDATTRIBUTE) :- attribute(IDATTRIBUTES, IDATTRIBUTE).
attribute(IDPARENT, IDATTRIBUTE, NAME, VALUE) :- name(IDATTRIBUTE, IDNAME,NAME), value(IDATTRIBUTE, IDVALUE,VALUE).

%provenancedata(IDPARENT, IDPROVENANCEDATA, IDVERTEX, IDEDGE) :- vertex(IDPROVENANCEDATA, IDVERTEX), edge(IDPROVENANCEDATA, IDEDGE).
%provenancedata(IDPARENT, IDPROVENANCEDATA, IDEDGE) :- edge(IDPROVENANCEDATA, IDEDGE).
%provenancedata(IDPARENT, IDPROVENANCEDATA, IDVERTEX) :- vertex(IDPROVENANCEDATA, IDVERTEX).
%edge(IDPARENT, IDEDGE, ID, TYPE, LABEL, VALUE, SOURCEID, TARGETID) :- id(IDEDGE, IDID,ID), type(IDEDGE, IDTYPE,TYPE), label(IDEDGE, IDLABEL,LABEL), value(IDEDGE, IDVALUE,VALUE), sourceid(IDEDGE, IDSOURCEID,SOURCEID), targetid(IDEDGE, IDTARGETID,TARGETID).
%vertex(IDPARENT, IDVERTEX, ID, TYPE, LABEL, DATE, IDATTRIBUTE) :- id(IDVERTEX, IDID,ID), type(IDVERTEX, IDTYPE,TYPE), label(IDVERTEX, IDLABEL,LABEL), date(IDVERTEX, IDDATE,DATE), attribute(IDVERTEX, IDATTRIBUTE).
%vertex(IDPARENT, IDVERTEX, ID, TYPE, LABEL, DATE) :- id(IDVERTEX, IDID,ID), type(IDVERTEX, IDTYPE,TYPE), label(IDVERTEX, IDLABEL,LABEL), date(IDVERTEX, IDDATE,DATE).
%attribute(IDPARENT, IDATTRIBUTE, NAME, VALUE) :- name(IDATTRIBUTE, IDNAME,NAME), value(IDATTRIBUTE, IDVALUE,VALUE).

%Directives
:- discontiguous provenancedata/4.
:- discontiguous vertices/3.
:- discontiguous edges/3.
:- discontiguous attributes/2.
:- discontiguous edge/8.
:- discontiguous vertex/7.
:- discontiguous vertex/6.
:- discontiguous attribute/4.
:- discontiguous targetid/3.
:- discontiguous sourceid/3.
:- discontiguous value/3.
:- discontiguous type/3.
:- discontiguous label/3.
:- discontiguous id/3.
:- discontiguous edge/2.
:- discontiguous name/3.
:- discontiguous attribute/2.
:- discontiguous vertex/2.
:- discontiguous date/3.


%
%NewRules
add(X, L, [X|L]).
threshold(X,Y,VAL) :- abs(X - Y) =< VAL.

%Path
neighbor(X, Y, LABEL) :- edge(_, _, _, _, LABEL, _, Y, X), vertex(_, _, X, TYPE, _, _, _), vertex(_, _, Y, TYPE, _, _, _).	

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
%attributes(L, ATT) :- findall(X, attribute_value(X, ATT),L).
attstddev(STD, ATT) :- 
	findall(X, attribute_value(X, ATT),L),
	stddev(L,STD).
	

%DummyFilter
filterlower(X, Y, Z) :- vertex(_, _, X, _, _, _, IDATTRIBUTES), attributes(_, IDATTRIBUTES, IDATTRIBUTE), attribute(_, IDATTRIBUTE, Y, VALUE), atom_number(VALUE, W), W < Z.
filtergreater(X, Y, Z) :- vertex(_, _, X, _, _, _, IDATTRIBUTES), attributes(_, IDATTRIBUTES, IDATTRIBUTE), attribute(_, IDATTRIBUTE, Y, VALUE), atom_number(VALUE, W), W > Z.

%set_prolog_flag(toplevel_print_options, [quoted(true), portray(true), max_depth(100), priority(699)]).
%CollapseFilter

filter_vertex_first(Lant,ATT,LABEL,STD,[X|Lresp]) :- 
	MIN is 999999 ,
	MAX is -999999 ,
	first(Lant,Y),
	neighbor(Y,X,LABEL), 
	vertex(_, _, Y, _, _, _, IDATTRIBUTES1), 
	vertex(_, _, X, _, _, _, IDATTRIBUTES2), 
	attributes(_, IDATTRIBUTES1, IDATTRIBUTE1),
	attributes(_, IDATTRIBUTES2, IDATTRIBUTE2),
	attribute(_, IDATTRIBUTE1, ATT, VALUE1), 
	attribute(_, IDATTRIBUTE2, ATT, VALUE2),
	atom_number(VALUE1, W1),
	atom_number(VALUE2, W2),
	max(W1,MAX,MX1),
	min(W1,MIN,MN1),
	max(W2,MX1,MX2),
	min(W2,MN1,MN2),
	threshold(MX2,MN2,STD),
	not(IDATTRIBUTE1 = IDATTRIBUTE2),
	filter_vertex_second([X|Lant],ATT,LABEL,MN2,MX2,STD,Lresp).

	
filter_vertex_second(_,ATT,LABEL,MIN,MAX,STD,[]).	
filter_vertex_second(Lant,ATT,LABEL,MIN,MAX,STD,[X|Lresp]) :- 
	first(Lant,Y),
	neighbor(Y,X,LABEL), 
	vertex(_, _, X, _, _, _, IDATTRIBUTES2), 
	attributes(_, IDATTRIBUTES2, IDATTRIBUTE2),
	attribute(_, IDATTRIBUTE2, ATT, VALUE2),
	atom_number(VALUE2, W2),
	max(W2,MAX,MX2),
	min(W2,MIN,MN2),
	threshold(MX2,MN2,STD),
	filter_vertex_second([X|Lant],ATT,LABEL,MN2,MX2,STD,Lresp).
	

collapse_irrelevant(R, ATT, LABEL, STD) :-
	filter_vertex_first([X],ATT,LABEL,STD,L),
	add(X,L,R).

	
collapse_vertices(R,ATT,LABEL) :- 
	attstddev(STD, ATT),
	setof(X,collapse_irrelevant(X,ATT,LABEL, STD),L),
	rem_sub_sets(L, R).
	
	
rem_sub_sets([], []).
rem_sub_sets([L|Ls], R) :-
    (   select(T, Ls, L1), % get any T in Ls
        ord_subset(L, T)   % is T a superset of L ?
    ->  rem_sub_sets([T|L1], R) % discard T, keep L for further tests
    ;   R = [L|L2],
        rem_sub_sets(Ls, L2)
    ).

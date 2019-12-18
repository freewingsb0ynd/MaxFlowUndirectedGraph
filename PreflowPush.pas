{$MODE OBJFPC}
program MaximumFlow;

const
  maxN = 50000;
  maxM = 1000000;
  maxC = 1000000;
type
  TEdge = record
    x, y: integer;
    c, f: integer;
  end;

  TQueue = record
    items: array[0..maxN - 1] of integer;
    front, rear, nItems: integer;
  end;
var
  e: array[-maxM..maxM] of TEdge;
  link: array[-maxM..maxM] of integer;
  head, current: array[1..maxN] of integer;
  excess: array[1..maxN] of integer;
  h: array[1..maxN] of integer;
  Count: array[0..2 * maxN - 1] of integer;
  Queue: TQueue;
  n, m, s, t: integer;
  FlowValue: integer;

procedure Enter;
var
  i, u, v, capacity: integer;
begin
  readln(n, m, s, t);
  FillChar(head[1], n * SizeOf(head[1]), 0);
  for i := 1 to m do
  begin
    readln(u, v, capacity);
    with e[i] do
    begin
      x := u;
      y := v;
      c := capacity;
      link[i] := head[u];
      head[u] := i;
    end;
    with e[-i] do
    begin
      x := v;
      y := u;
      c := capacity;
      link[-i] := head[v];
      head[v] := -i;
    end;
  end;
  for v := 1 to n do
    current[v] := head[v];
end;

procedure PushToQueue(v: integer);
begin
  with Queue do
  begin
    rear := (rear + 1) mod maxN;
    items[rear] := v;
    Inc(nItems);
  end;
end;

function PopFromQueue: integer;
begin
  with Queue do
  begin
    result := items[front];
    front := (front + 1) mod maxN;
    Dec(nItems);
  end;
end;

procedure Init;
var
  v, sf, i: integer;
begin
  for i := -m to m do
    e[i].f := 0;
  FillChar(excess[1], n * SizeOf(excess[1]), 0);
  i := head[s];
  while i <> 0 do
  begin
    sf := e[i].c;
    e[i].f := sf;
    e[-i].f := -sf;
    Inc(excess[e[i].y], sf);
    Dec(excess[s], sf);
    i := link[i];
  end;
  for v := 1 to n do
    h[v] := 1;
  h[s] := n;
  h[t] := 0;
  FillChar(Count[0], (2 * n) * SizeOf(Count[0]), 0);
  Count[n] := 1;
  Count[0] := 1;
  Count[1] := n - 2;
  Queue.front := 0;
  Queue.rear := -1;
  Queue.nItems := 0;
  for v := 1 to n do
    if (v <> s) and (v <> t) and (excess[v] > 0) then
      PushToQueue(v);
end;

procedure Push(i: integer);
var
  Delta: integer;
begin
  with e[i] do
    if excess[x] < c - f then
      Delta := excess[x]
    else
      Delta := c - f;
  Inc(e[i].f, Delta);
  Dec(e[-i].f, Delta);
  with e[i] do
  begin
    Dec(excess[x], Delta);
    Inc(excess[y], Delta);
  end;
end;

procedure SetH(u: integer; NewH: integer);
begin
  Dec(Count[h[u]]);
  h[u] := NewH;
  Inc(Count[NewH]);
end;

procedure PerformGapHeuristic(gap: integer);
var
  v: integer;
begin
  if (0 < gap) and (gap < n) and (Count[gap] = 0) then
    for v := 1 to n do
      if (v <> s) and (gap < h[v]) and (h[v] <= n) then
      begin
        SetH(v, n + 1);
        current[v] := head[v];
      end;
end;

procedure Lift(u: integer);
var
  minH, OldH, i: integer;
begin
  minH := 2 * maxN;
  i := head[u];
  while i <> 0 do
  begin
    with e[i] do
      if (c > f) and (h[y] < minH) then
        minH := h[y];
    i := link[i];
  end;
  OldH := h[u];
  SetH(u, minH + 1);
  PerformGapHeuristic(OldH);
end;

procedure FIFOPreflowPush;
var
  NeedQueue: boolean;
  z: integer;
begin
  while Queue.nItems > 0 do
  begin
    z := PopFromQueue;
    while current[z] <> 0 do
    begin
      with e[current[z]] do
        if (c > f) and (h[x] > h[y]) then
        begin
          NeedQueue := (y <> s) and (y <> t) and (excess[y] = 0);
          Push(current[z]);
          if NeedQueue then
            PushToQueue(y);
          if excess[z] = 0 then
            break;
        end;
      current[z] := link[current[z]];
    end;
    if excess[z] > 0 then
    begin
      Lift(z);
      current[z] := head[z];
      PushToQueue(z);
    end;
  end;
  FlowValue := excess[t];
end;

procedure PrintResult;
var
  i: integer;
begin
  writeln('Value of flow: ', FlowValue);
end;

begin
  Enter;
  Init;
  FIFOPreflowPush;
  PrintResult;
end.
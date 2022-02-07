#! /usr/bin/env python3

import json


def get_edges(graph):
    for d in json.loads(graph):
        node = d['data']
        yield node['source'], node['target']


def plot(graph, outfile='graph.txt'):
    with open(outfile, 'w') as fout:
        for src, dst in get_edges(graph):
            fout.write('%s  %s\n' % (src, dst))


if __name__ == '__main__':
    print("Hello, World!")
    plot('
    [ { "data": { "cost": 0.010000, "source": "HBA2", "target": "HBB" }},
      { "data": { "cost": 0.598835, "source": "HBA2", "target": "EGFR" }},
      { "data": { "cost": 0.594442, "source": "HBA2", "target": "DAXX" }},
      { "data": { "cost": 0.598835, "source": "HBA2", "target": "PBK" }},
      { "data": { "cost": 0.598835, "source": "HBA2", "target": "MAPK14" }},
      { "data": { "cost": 0.598835, "source": "HBA2", "target": "MST4" }}
    ]
    ')

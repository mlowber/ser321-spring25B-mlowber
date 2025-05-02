package example.grpcclient;

import io.grpc.stub.StreamObserver;
import service.SortGrpc;
import service.SortRequest;
import service.SortResponse;
import service.Algo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class SortImpl extends SortGrpc.SortImplBase {

    @Override
    public void sort(SortRequest req, StreamObserver<SortResponse> obs) {
        SortResponse.Builder respB = SortResponse.newBuilder();
        List<Integer> input = req.getDataList();
        List<Integer> sorted = new ArrayList<>(input);

        try {
            switch (req.getAlgo()) {
                case MERGE:
                    sorted = mergeSort(sorted);
                    break;
                case QUICK:
                    sorted = quickSort(sorted);
                    break;
                case INTERN:
                    Collections.sort(sorted);
                    break;
                default:
                    throw new IllegalArgumentException("Unsupported Algo: " + req.getAlgo());
            }
            respB.setIsSuccess(true).addAllData(sorted);

        } catch (Exception e) {
            respB.setIsSuccess(false)
                    .setError("Sort failed: " + e.getMessage());
        }

        obs.onNext(respB.build());
        obs.onCompleted();
    }

    private List<Integer> mergeSort(List<Integer> list) {
        if (list.size() <= 1) return list;
        int mid = list.size()/2;
        List<Integer> left  = mergeSort(new ArrayList<>(list.subList(0, mid)));
        List<Integer> right = mergeSort(new ArrayList<>(list.subList(mid, list.size())));
        return merge(left, right);
    }

    private List<Integer> merge(List<Integer> L, List<Integer> R) {
        List<Integer> out = new ArrayList<>();
        int i=0, j=0;
        while (i < L.size() && j < R.size()) {
            if (L.get(i) <= R.get(j)) out.add(L.get(i++));
            else                    out.add(R.get(j++));
        }
        while (i < L.size()) out.add(L.get(i++));
        while (j < R.size()) out.add(R.get(j++));
        return out;
    }

    private List<Integer> quickSort(List<Integer> list) {
        if (list.size() <= 1) return list;
        int pivot = list.get(list.size()/2);
        List<Integer> less    = list.stream().filter(x -> x < pivot).collect(Collectors.toList());
        List<Integer> equal   = list.stream().filter(x -> x == pivot).collect(Collectors.toList());
        List<Integer> greater = list.stream().filter(x -> x > pivot).collect(Collectors.toList());
        List<Integer> sorted = new ArrayList<>();
        sorted.addAll(quickSort(less));
        sorted.addAll(equal);
        sorted.addAll(quickSort(greater));
        return sorted;
    }
}

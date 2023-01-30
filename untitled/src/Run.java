
class Solution {
    int m = (int)(Math.pow(10, 9) + 7);
    long power(int n, long x)
    {
        long res = 1;
        while (n > 0) {
            if (n % 2 == 1)
                res = ((res % m)*(x % m)) % m;
            n = n >> 1;
            x = ((x % m) * (x % m)) % m;
        }
        return res;
    }
    public int monkeyMove(int n) {
        return (((int)power(n, 2) % m) - 2) % m;
    }
}
public class Run{
    public static void main(String[] args) {
        Solution s = new Solution();
        s.monkeyMove(500000003);
    }
}
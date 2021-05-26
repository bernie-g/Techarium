package software.bernie.techarium.util;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class Both<A,B> {
    A left;
    B right;
}

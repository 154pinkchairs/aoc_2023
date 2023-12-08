defmodule Part2 do
  def to_int(str) do
    numbers = ["zero", "one", "two", "three", "four", "five", "six", "seven", "eight", "nine"]

    String.replace(str, numbers, fn x ->
      Enum.find_index(numbers, &(&1 == x))
    end)
    |> to_charlist()
    |> inspect(charlists: :as_list)
    |> String.replace(~r/[\[\]]/, "")
    |> String.to_integer()
  end

  def sum_values(file) do
    digits = "zero|one|two|three|four|five|six|seven|eight|nine|[0-9]"
    re = ~r/(^#{digits})|(#{digits}$)/

    f = File.read!("lib/input.txt")

    contents =
      String.split(f, "\n")
      |> Enum.map(fn line ->
        line = String.replace(line, "twone", "twoone")

        Regex.split(
          re,
          line,
          include_captures: true
        )

        # remove empty strings and strings that don't contain digits
        |> Enum.reject(&(&1 == "" || Regex.scan(~r/(#{digits})/, &1) == []))
      end)

    # get first and last elements of each list
    first_last =
      Enum.map(contents, fn elem ->
        case Enum.count(elem) do
          1 -> elem
          _ -> [Enum.at(elem, 0), Enum.at(elem, -1)]
        end
      end)

    # iterate over each line
    Enum.map(first_last, fn line ->
      Enum.map(line, fn inner ->
        if inner != nil do
          case Integer.parse(inner) do
            {int, _} -> int
            :error -> Part2.to_int(inner)
          end
        end
      end)
      |> Enum.join()
    end)
    |> Enum.reject(&(&1 == ""))
    |> Enum.map(fn num -> String.to_integer(num) end)
    |> Enum.sum()
  end
end
